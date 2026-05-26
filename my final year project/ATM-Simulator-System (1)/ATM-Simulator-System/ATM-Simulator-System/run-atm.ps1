param(
    [string]$DbUser = $(if ($env:ATM_DB_USER) { $env:ATM_DB_USER } else { "root" }),
    [string]$DbPassword = $env:ATM_DB_PASSWORD
)

$java = (Get-Command java.exe -ErrorAction SilentlyContinue).Source
$javac = (Get-Command javac.exe -ErrorAction SilentlyContinue).Source
$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$classes = Join-Path $projectDir "build\classes"
$lib = Join-Path $projectDir "lib"
$mysql = Join-Path $lib "mysql-connector-j-8.4.0.jar"
$jcalendar = Join-Path $lib "jcalendar-1.4.jar"
$log = Join-Path $projectDir "atm.log"

if (!$java -or !(Test-Path $java)) {
    Write-Error "Java was not found on PATH"
    exit 1
}

if (!(Test-Path $mysql)) {
    Write-Error "MySQL Connector/J jar was not found at $mysql"
    exit 1
}

if (!(Test-Path $jcalendar)) {
    Write-Error "JCalendar jar was not found at $jcalendar"
    exit 1
}

if ($null -eq $DbPassword) {
    $securePassword = Read-Host "Enter MySQL password for user '$DbUser' (press Enter if blank)" -AsSecureString
    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    try {
        $DbPassword = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
    } finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
    }
}

if ($javac) {
    New-Item -ItemType Directory -Force $classes | Out-Null
    $sources = Get-ChildItem -Path (Join-Path $projectDir "src") -Recurse -Filter *.java | ForEach-Object { $_.FullName }
    & $javac -cp "$lib\*" -d $classes $sources
    if ($LASTEXITCODE -ne 0) {
        Write-Error "Compilation failed"
        exit $LASTEXITCODE
    }
    Copy-Item -Path (Join-Path $projectDir "src\ASimulatorSystem\icons") -Destination (Join-Path $classes "ASimulatorSystem") -Recurse -Force
}

$oldProcesses = Get-CimInstance Win32_Process -Filter "name = 'java.exe'" |
        Where-Object { $_.CommandLine -like "*ASimulatorSystem.*" -or $_.CommandLine -like "*ATM-Simulator-System*" }
foreach ($oldProcess in $oldProcesses) {
    Stop-Process -Id $oldProcess.ProcessId -Force -ErrorAction SilentlyContinue
}

$classpath = "$classes;$mysql;$jcalendar"
$env:ATM_DB_USER = $DbUser
$env:ATM_DB_PASSWORD = $DbPassword
$javaArgs = @(
    "-cp",
    $classpath,
    "ASimulatorSystem.Login"
)
& $java @javaArgs 2>&1 | Tee-Object -FilePath $log
