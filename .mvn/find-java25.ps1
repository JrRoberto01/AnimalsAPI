$jdk = Get-ChildItem 'C:\Program Files\JetBrains\IntelliJ IDEA *\jbr' `
    -Directory `
    -ErrorAction SilentlyContinue |
    Where-Object {
        $releaseFile = Join-Path $_.FullName 'release'
        (Test-Path (Join-Path $_.FullName 'bin\java.exe')) -and
        ((Get-Content $releaseFile -ErrorAction SilentlyContinue) -match 'JAVA_VERSION="25')
    } |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1

if ($jdk) {
    $jdk.FullName
}
