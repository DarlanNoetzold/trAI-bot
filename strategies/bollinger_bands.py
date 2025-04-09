import argparse
import time
import requests
import statistics
import re

def parse_interval(interval_str):
    match = re.match(r'^(\d+)([smh])$', interval_str.lower())
    if not match:
        raise argparse.ArgumentTypeError("Formato de intervalo inválido. Use ex: 10s, 1m, 2h")
    value, unit = match.groups()
    value = int(value)
    if unit == 's':
        return value
    elif unit == 'm':
        return value * 60
    elif unit == 'h':
        return value * 3600

def get_price(symbol):
    try:
        response = requests.get(f"http://localhost:8080/api/market/price?symbol={symbol}")
        return float(response.json()["price"])
    except Exception as e:
        print("Erro ao buscar preço:", e)
        return None

def calculate_bollinger_bands(prices, window=20, num_std_dev=2):
    if len(prices) < window:
        return None, None, None
    sma = sum(prices[-window:]) / window
    std_dev = statistics.stdev(prices[-window:])
    upper_band = sma + num_std_dev * std_dev
    lower_band = sma - num_std_dev * std_dev
    return lower_band, sma, upper_band

def execute_strategy(symbol, interval_seconds):
    prices = []

    while True:
        price = get_price(symbol)
        if price is not None:
            print(symbol)
            prices.append(price)
            print(f"Preço atual: {price}")

            lower, middle, upper = calculate_bollinger_bands(prices)
            if lower and upper:
                print(f"Bollinger Bands - Inferior: {lower}, Média: {middle}, Superior: {upper}")
                if price < lower:
                    print("Sinal de COMPRA (abaixo da banda inferior)")
                elif price > upper:
                    print("Sinal de VENDA (acima da banda superior)")
        else:
            print("Erro ao obter preço.")

        time.sleep(interval_seconds)

if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--symbol", required=True, help="Símbolo da moeda (ex: BTCUSDT)")
    parser.add_argument("--interval", type=parse_interval, default="10s", help="Intervalo (ex: 10s, 1m, 2h)")

    args = parser.parse_args()

    execute_strategy(args.symbol, args.interval)
