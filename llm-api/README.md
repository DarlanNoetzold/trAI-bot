
# Local LLM API

## Overview

This project is a RESTful API built with Spring Boot that allows you to interact with locally hosted Large Language Models (LLMs) using the Spring AI library. It enables users to send prompts to an LLM server (e.g., Ollama) and receive generated responses in real-time.

## Technologies Used

- **Spring Boot**: For creating robust, production-grade web services.
- **Spring AI**: Provides an easy abstraction layer to interact with different AI and LLM backends.
- **WebFlux (Reactive Programming)**: Ensures efficient, non-blocking I/O handling.
- **Ollama**: Example local LLM server (e.g., Llama, GPT-like models).

## Prerequisites

- **Java 17 or later**
- **Maven** or **Gradle**
- A local LLM server (like Ollama) running and accessible.

## Installation

1. **Clone the repository:**

```bash
git clone <repository-url>
```

2. **Navigate into the project directory:**

```bash
cd llm-api
```

3. **Build the project:**

```bash
mvn clean install
```

4. **Run the application:**

```bash
mvn spring-boot:run
```

The API server will start on port `8080` by default.

## Configuration

### Application Properties

Configure the API endpoint for your local LLM server in `src/main/resources/application.properties`:

```properties
spring.ai.ollama.base-url=http://localhost:11434
```

Change `localhost:11434` to the IP and port of your local LLM service.

## API Endpoints

### POST `/ollama`

**Description:** Sends a prompt to the configured local LLM and returns a streaming response.

**Request:**

- **Body:** Plain text (your prompt).

**Response:**

- **Type:** `text/event-stream` (Flux)
- **Content:** Streaming response from the LLM.

**Example curl request:**

```bash
curl -X POST http://localhost:8080/ollama \
     -H "Content-Type: text/plain" \
     -d "Can you give an example of a leet style coding problem and answer it in Java"
```

## Example Controller

```java
package tech.noetzold.llm_api;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class OllamaChatController {

    private final ChatClient chatClient;

    public OllamaChatController(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/ollama")
    public Flux<String> ollama(@RequestBody String userPrompt) {
        return chatClient.prompt()
                .user(userPrompt)
                .stream()
                .content();
    }
}
```

## Troubleshooting Common Errors

- **Connection errors:**
  - Verify the local LLM server is running.
  - Ensure the correct IP/port configuration.
  - Check firewall or network settings if running remotely.

- **HTTP/1.1 header parser errors:**
  - Usually caused by network or protocol issues.
  - Verify HTTP/HTTPS settings match your LLM server.

## Future Improvements

- Implement additional endpoints for chat history management.
- Support multiple LLM backends seamlessly.
- Add authentication and security features.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## License

Distributed under the MIT License. See `LICENSE` for more information.

## Acknowledgments

- Spring Boot Team
- Spring AI Contributors
- Ollama Community



⭐️ From [DarlanNoetzold](https://github.com/DarlanNoetzold)
