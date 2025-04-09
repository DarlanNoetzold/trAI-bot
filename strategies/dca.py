import time
import requests

def execute(symbol="BTCUSDT", investment=100, interval_sec=3600, repetitions=3):
    price_url = f"https://api.binance.com/api/v3/ticker/price?symbol={symbol}"
    for i in range(repetitions):
        price = float(requests.get(price_url).json()['price'])
        amount = investment / price
        print(f"[DCA] Executando compra de {amount:.6f} {symbol} a {price} USDT (repetição {i+1})")
        time.sleep(interval_sec)

if __name__ == "__main__":
    execute()