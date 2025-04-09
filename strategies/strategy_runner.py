import subprocess
import os
from typing import List

STRATEGY_DIR = os.path.join(os.path.dirname(__file__), '../strategies')

class StrategyRunner:

    @staticmethod
    def list_strategies() -> List[str]:
        """Lista todos os scripts de estratégia disponíveis."""
        return [f for f in os.listdir(STRATEGY_DIR) if f.endswith(".py")]

    @staticmethod
    def run_strategy(script_name: str, parameters: List[str] = []) -> str:
        """Executa uma estratégia pelo nome do script e parâmetros."""
        script_path = os.path.join(STRATEGY_DIR, script_name)
        if not os.path.isfile(script_path):
            raise FileNotFoundError(f"Script {script_name} não encontrado em {STRATEGY_DIR}")

        command = ["python3", script_path] + parameters

        try:
            result = subprocess.run(command, capture_output=True, text=True, check=True)
            return result.stdout
        except subprocess.CalledProcessError as e:
            return f"Erro ao executar a estratégia: {e.stderr}"
