package aichelberg2.drl.extension;

import org.eclipse.lsp4j.jsonrpc.Launcher;
import org.eclipse.lsp4j.services.LanguageClient;

import java.util.Scanner;

public class Main
{
  public static void main(String[] args)
  {
    // Create an instance of your language server
    MyLanguageServer server = new MyLanguageServer();

    // Create a new Launcher that connects your server to the client's input and output streams
    Launcher<LanguageClient> launcher = Launcher.createLauncher(server, LanguageClient.class, System.in, System.out);

    // Connect your server instance with the remote language client
    LanguageClient client = launcher.getRemoteProxy();
    server.connect(client);

    // Start listening for requests
    launcher.startListening();
  }
}
