FROM prom/prometheus:v2.52.0

# Copia o prometheus.yml para o diretório correto dentro do container
COPY prometheus.yml /etc/prometheus/prometheus.yml

# Define o diretório de trabalho
WORKDIR /etc/prometheus

# Comando correto para rodar o Prometheus
CMD ["--config.file=/etc/prometheus/prometheus.yml", "--storage.tsdb.path=/prometheus"]
