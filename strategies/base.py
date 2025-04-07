# strategies/base.py
from abc import ABC, abstractmethod

class BaseStrategy(ABC):
    @abstractmethod
    def execute(self, market_data: dict, wallet: dict) -> dict:
        pass
