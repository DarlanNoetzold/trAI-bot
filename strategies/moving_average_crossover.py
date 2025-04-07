import time
import requests

def get_price(symbol):
    try:
        response = requests.get(f"http://localhost:8080/api/market/price?symbol={symbol}")
        return float(response.json()["price"])
    except Exception as e:
        print("Erro ao buscar preço:", e)
        return None

def moving_average(prices, window):
    if len(prices) < window:
        return None
    return sum(prices[-window:]) / window

def execute_strategy(symbol):
    short_window = 5
    long_window = 20
    prices = []

    while True:
        price = get_price(symbol)
        if price is not None:
            prices.append(price)
            print(f"Preço atual: {price}")

            short_ma = moving_average(prices, short_window)
            long_ma = moving_average(prices, long_window)

            if short_ma and long_ma:
                print(f"MA Curta ({short_window}): {short_ma}")
                print(f"MA Longa ({long_window}): {long_ma}")

                if short_ma > long_ma:
                    print("🔼 Sinal de COMPRA (Crossover positivo)")
                elif short_ma < long_ma:
                    print("🔽 Sinal de VENDA (Crossover negativo)")
        else:
            print("Preço inválido, tentando novamente...")

        time.sleep(10)

if __name__ == "__main__":
    import sys
    symbol = sys.argv[1] if len(sys.argv) > 1 else "BTCUSDT"
    execute_strategy(symbol)