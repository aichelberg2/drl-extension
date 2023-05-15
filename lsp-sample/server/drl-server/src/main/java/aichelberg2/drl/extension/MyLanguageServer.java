package aichelberg2.drl.extension;

import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.services.*;

import java.util.concurrent.CompletableFuture;

public class MyLanguageServer implements LanguageServer
{
  private TextDocumentService textService;
  private WorkspaceService workspaceService;
  private LanguageClient client;

  public void connect(LanguageClient client)
  {
    this.client = client;
  }


  @Override
  public CompletableFuture<InitializeResult> initialize(InitializeParams params)
  {
    this.textService = new MyTextDocumentService(client);
    this.workspaceService = new MyWorkspaceService();

    ServerCapabilities capabilities = new ServerCapabilities();
    capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
    InitializeResult result = new InitializeResult(capabilities);

    // Send a "Hello, World!" message to the client
    MessageParams message = new MessageParams();
    message.setType(MessageType.Info);
    message.setMessage("Hello, World!");
    client.showMessage(message);

    return CompletableFuture.completedFuture(result);
  }

  @Override
  public CompletableFuture<Object> shutdown()
  {
    // Add your shutdown logic here
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public void exit()
  {
    System.exit(0);
  }

  @Override
  public TextDocumentService getTextDocumentService()
  {
    return this.textService;
  }

  @Override
  public WorkspaceService getWorkspaceService()
  {
    return this.workspaceService;
  }
}
