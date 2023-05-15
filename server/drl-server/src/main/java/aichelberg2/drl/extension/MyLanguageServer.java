package aichelberg2.drl.extension;

import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.compiler.DroolsError;
import org.drools.compiler.compiler.DroolsParserException;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyLanguageServer implements LanguageServer
{
  private WorkspaceService workspaceService;
  private LanguageClient client;
  private TextDocumentService textService = new TextDocumentService()
  {

    @Override
    public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position)
    {
      return null;
    }

    // Handle when a document is opened
    @Override
    public void didOpen(DidOpenTextDocumentParams params)
    {
      validateDocument(params.getTextDocument().getUri(), params.getTextDocument().getText());
    }

    // Handle when a document is changed
    @Override
    public void didChange(DidChangeTextDocumentParams params)
    {
      for (TextDocumentContentChangeEvent changeEvent : params.getContentChanges())
      {
        validateDocument(params.getTextDocument().getUri(), changeEvent.getText());
      }
    }

    // Handle when a document is closed
    @Override
    public void didClose(DidCloseTextDocumentParams params)
    {
      // Nothing to do when document is closed
    }

    @Override
    public void didSave(DidSaveTextDocumentParams params)
    {
      validateDocument(params.getTextDocument().getUri(), params.getText());
    }
  };

  public void connect(LanguageClient client)
  {
    this.client = client;
  }

  @Override
  public CompletableFuture<InitializeResult> initialize(InitializeParams params)
  {
    ServerCapabilities capabilities = new ServerCapabilities();
    capabilities.setTextDocumentSync(TextDocumentSyncKind.Full);
    InitializeResult result = new InitializeResult(capabilities);
    return CompletableFuture.completedFuture(result);
  }

  @Override
  public CompletableFuture<Object> shutdown()
  {
    // Add your shutdown logic here
    return CompletableFuture.completedFuture(null);
  }

  private void validateDocument(String uri, String content)
  {
    try
    {
      DrlParser parser = new DrlParser();
      parser.parse(true, content);

      List<DroolsError> errors = parser.getErrors();
      List<Diagnostic> diagnostics = new ArrayList<>();

      for (DroolsError error : errors)
      {
        Diagnostic diagnostic = new Diagnostic();
        diagnostic.setSeverity(DiagnosticSeverity.Error);
        diagnostic.setMessage(error.getMessage());
        diagnostic.setRange(new Range(new Position(error.getLines()[0], 0), new Position(error.getLines()[0], 0))); // Set the range
        // to cover
        // the entire
        // document
        diagnostics.add(diagnostic);
      }

      LanguageClient client = this.client;
      client.publishDiagnostics(new PublishDiagnosticsParams(uri, diagnostics));
    }
    catch (DroolsParserException e)
    {
      // Handle parser exception if needed
    }
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
