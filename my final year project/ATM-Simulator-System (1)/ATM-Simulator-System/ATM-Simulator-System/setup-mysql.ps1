param(
    [string]$DbUser = $(if ($env:ATM_DB_USER) { $env:ATM_DB_USER } else { "root" }),
    [string]$DbPassword = $env:ATM_DB_PASSWORD
)

$projectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$schema = Join-Path $projectDir "database_setup.sql"
$mysql = (Get-Command mysql.exe -ErrorAction SilentlyContinue).Source

if (!$mysql) {
    $defaultMysql = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
    if (Test-Path $defaultMysql) {
        $mysql = $defaultMysql
    }
}

if (!$mysql -or !(Test-Path $mysql)) {
    Write-Error "mysql.exe was not found. Add MySQL Server bin to PATH or install MySQL Server 8.0."
    exit 1
}

if (!(Test-Path $schema)) {
    Write-Error "Database setup file was not found at $schema"
    exit 1
}

if (!$DbPassword) {
    $securePassword = Read-Host "Enter MySQL password for user '$DbUser' (press Enter if blank)" -AsSecureString
    $bstr = [Runtime.InteropServices.Marshal]::SecureStringToBSTR($securePassword)
    try {
        $DbPassword = [Runtime.InteropServices.Marshal]::PtrToStringBSTR($bstr)
    } finally {
        [Runtime.InteropServices.Marshal]::ZeroFreeBSTR($bstr)
    }
}

Get-Content $schema | & $mysql -u $DbUser "-p$DbPassword"
if ($LASTEXITCODE -ne 0) {
    Write-Error "MySQL database setup failed"
    exit $LASTEXITCODE
}

Write-Host "MySQL database 'bankmanagementsystem' is ready."
