export async function testConnection() {
    const response = await fetch('/prosecutor/test');
    const body = await response.text();

    if (response.ok) return body
}
