import requests

URL = "http://1d3p.wp.codeforces.com/new"

HEADERS = {
        "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
        "Accept-Encoding": "gzip, deflate",
        "Accept-Language": "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7",
        "Cache-Control": "max-age=0",
        "Connection": "keep-alive",
        "Content-Length": "51",
        "Content-Type": "application/x-www-form-urlencoded",
        "Cookie": "JSESSIONID=8EF6C8C42641099B12AE567917ADF0DC",
        "DNT": "1",
        "Host": "1d3p.wp.codeforces.com",
        "Origin": "http://1d3p.wp.codeforces.com",
        "Referer": "http://1d3p.wp.codeforces.com/",
        "Upgrade-Insecure-Requests": "1",
        "User-Agent": "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/150.0.0.0 Safari/537.36"}

def make_request(num):
    data = {
            "_af": "34be50b38beccce4",
            "proof": str(num * num),
            "amount": str(num),
            "submit": "Submit"
            }

    res = requests.post(url=URL, headers=HEADERS, data=data, verify=False)
    
    if not res.status_code or num % 10 == 0:
        print(f"==========\nrequest for: ${num}")
        print(f"==========\nresult: ${res.status_code}")
        print(f"==========\nheaders: ${res.headers}")
        print(f"==========\nbody: ${res.text}")


for i in range(1, 101):
    make_request(i)

