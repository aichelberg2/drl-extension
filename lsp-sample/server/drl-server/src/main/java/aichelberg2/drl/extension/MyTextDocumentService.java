package aichelberg2.drl.extension;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.DrlParser;
import org.drools.compiler.lang.descr.PackageDescr;
import org.eclipse.lsp4j.*;
import org.eclipse.lsp4j.jsonrpc.messages.Either;
import org.eclipse.lsp4j.services.LanguageClient;
import org.eclipse.lsp4j.services.TextDocumentService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MyTextDocumentService implements TextDocumentService {

  private final DrlParser parser = new DrlParser();
  private final LanguageClient client;

  public MyTextDocumentService(LanguageClient client) {
    this.client = client;
  }

  @Override
  public CompletableFuture<Either<List<CompletionItem>, CompletionList>> completion(CompletionParams position) {
    return null;
  }

  // Handle when a document is opened
  @Override
  public void didOpen(DidOpenTextDocumentParams params) {
    validateDocument(params.getTextDocument().getUri(), params.getTextDocument().getText());
  }

  // Handle when a document is changed
  @Override
  public void didChange(DidChangeTextDocumentParams params) {
    for (TextDocumentContentChangeEvent changeEvent : params.getContentChanges()) {
      validateDocument(params.getTextDocument().getUri(), changeEvent.getText());
    }
  }

  // Handle when a document is closed
  @Override
  public void didClose(DidCloseTextDocumentParams params) {
    // Nothing to do when document is closed
  }

  @Override
  public void didSave(DidSaveTextDocumentParams params) {
    validateDocument(params.getTextDocument().getUri(), params.getText());
  }


  private void validateDocument(String uri, String content) {
    try {
      PackageDescr descr = parser.parse(true, content);
      // If parsing is successful, clear diagnostics
      LanguageClient client = this.client; // You'll need to get a reference to your client here
      client.publishDiagnostics(new PublishDiagnosticsParams(uri, new ArrayList<>()));
    } catch (DroolsParserException e) {
      // If parsing fails, report a diagnostic
      Diagnostic diagnostic = new Diagnostic();
      diagnostic.setSeverity(DiagnosticSeverity.Error);
      diagnostic.setMessage(e.getMessage());
      LanguageClient client = this.client; // You'll need to get a reference to your client here
      client.publishDiagnostics(new PublishDiagnosticsParams(uri, List.of(diagnostic)));
    }
  }

  // Other methods...
}
