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
import java.util.stream.Collectors;

@Slf4j
@Service
public class StrategyRunnerService {

    private static final Path BASE_DIR = Paths.get(System.getProperty("user.dir")).getParent(); // sobe um nível
    private static final Path STRATEGY_FOLDER = BASE_DIR.resolve("strategies");

    public List<String> listAvailableStrategies() {
        File folder = STRATEGY_FOLDER.toFile();
        if (!folder.exists() || !folder.isDirectory()) {
            log.warn("📁 Pasta de estratégias não encontrada: {}", STRATEGY_FOLDER);
            return Collections.emptyList();
        }

        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(file -> file.getName().endsWith(".py"))
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public String runStrategy(String strategyName, Map<String, String> params) {
        Path scriptPath = STRATEGY_FOLDER.resolve(strategyName + ".py");

        List<String> command = new ArrayList<>();
        command.add("python");
        command.add(scriptPath.toAbsolutePath().toString());
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

            Files.deleteIfExists(scriptPath); // limpeza

            return output;
        } catch (Exception e) {
            log.error("❌ Erro ao executar script customizado: {}", e.getMessage(), e);
            return "Erro ao executar script customizado: " + e.getMessage();
        }
    }

    private String executeCommand(List<String> command) {
        try {
            log.info("🚀 Executando comando: {}", String.join(" ", command));

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
