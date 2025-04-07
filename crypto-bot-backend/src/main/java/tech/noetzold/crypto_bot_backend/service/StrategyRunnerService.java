package tech.noetzold.crypto_bot_backend.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StrategyRunnerService {

    private static final Path BASE_DIR = Paths.get(System.getProperty("user.dir")).getParent();
    private static final Path STRATEGY_FOLDER = BASE_DIR.resolve("strategies");
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<String, Future<?>> runningStrategies = new ConcurrentHashMap<>();

    public List<String> listAvailableStrategies() {
        File folder = STRATEGY_FOLDER.toFile();
        if (!folder.exists() || !folder.isDirectory()) {
            log.warn("\uD83D\uDCC1 Pasta de estratégias não encontrada: {}", STRATEGY_FOLDER);
            return Collections.emptyList();
        }

        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(file -> file.getName().endsWith(".py"))
                .map(file -> file.getName().replace(".py", ""))
                .collect(Collectors.toList());
    }

    public String runStrategy(String strategyName, Map<String, String> params) {
        Path mainScriptPath = STRATEGY_FOLDER.resolve("main.py");

        List<String> command = new ArrayList<>();
        command.add("python");
        command.add(mainScriptPath.toAbsolutePath().toString());
        command.add("--strategy=" + strategyName);
        params.forEach((key, value) -> command.add("--" + key + "=" + value));

        return executeCommand(command);
    }

    public String runCustomScript(String code, Map<String, String> params) {
        try {
            String filename = "custom_strategy_" + UUID.randomUUID() + ".py";
            Path scriptPath = STRATEGY_FOLDER.resolve(filename);
            Files.writeString(scriptPath, code);

            List<String> command = new ArrayList<>();
            command.add("python");
            command.add(scriptPath.toAbsolutePath().toString());
            params.forEach((key, value) -> command.add("--" + key + "=" + value));

            String output = executeCommand(command);
            Files.deleteIfExists(scriptPath);

            return output;
        } catch (Exception e) {
            log.error("❌ Erro ao executar script customizado: {}", e.getMessage(), e);
            return "Erro ao executar script customizado: " + e.getMessage();
        }
    }

    public String runStrategyAsync(String strategyName, Map<String, String> params) {
        if (runningStrategies.containsKey(strategyName)) {
            return "Estratégia já está em execução.";
        }

        Future<?> future = executor.submit(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    runStrategy(strategyName, params);
                    Thread.sleep(60000); // executa a cada 60 segundos
                }
            } catch (InterruptedException e) {
                log.info("\uD83D\uDEAB Execução da estratégia '{}' foi interrompida.", strategyName);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("❌ Erro durante execução da estratégia '{}': {}", strategyName, e.getMessage(), e);
            }
        });

        runningStrategies.put(strategyName, future);
        return "Execução da estratégia iniciada: " + strategyName;
    }

    public String stopStrategy(String strategyName) {
        Future<?> future = runningStrategies.remove(strategyName);
        if (future != null) {
            future.cancel(true);
            return "Execução da estratégia interrompida: " + strategyName;
        } else {
            return "Estratégia não estava em execução: " + strategyName;
        }
    }

    private String executeCommand(List<String> command) {
        try {
            log.info("\uD83D\uDE80 Executando comando: {}", String.join(" ", command));
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.lines().collect(Collectors.joining("\n"));

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                log.error("❌ Código de saída: {}", exitCode);
                return "Erro na execução do script. Código de saída: " + exitCode + "\n" + output;
            }

            return output;
        } catch (Exception e) {
            log.error("❌ Erro ao executar comando: {}", e.getMessage(), e);
            return "Erro na execução: " + e.getMessage();
        }
    }
}
