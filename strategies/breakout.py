import requests
import time

def fetch_candles(symbol="BTCUSDT", interval="5m", limit=50):
    url = f"http://localhost:8080/api/market/candles?symbol={symbol}&interval={interval}&limit={limit}"
    response = requests.get(url)
    return response.json()

def main():
    symbol = "BTCUSDT"
    while True:
        candles = fetch_candles(symbol=symbol)
        highs = [float(c[2]) for c in candles[:-1]]
        lows = [float(c[3]) for c in candles[:-1]]
        last_close = float(candles[-1][4])

        resistance = max(highs)
        support = min(lows)

        print(f"📊 Breakout - Last Close: {last_close}, Resistance: {resistance}, Support: {support}")

        if last_close > resistance:
            print("🚀 Breakout para cima! BUY.")
        elif last_close < support:
            print("📉 Breakout para baixo! SELL.")
        else:
            print("⏸ HOLD - Preço dentro do canal.")
        time.sleep(60)

if __name__ == "__main__":
    main()