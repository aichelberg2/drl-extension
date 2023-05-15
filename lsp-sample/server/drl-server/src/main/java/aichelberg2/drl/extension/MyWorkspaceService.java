package aichelberg2.drl.extension;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.lsp4j.DidChangeConfigurationParams;
import org.eclipse.lsp4j.DidChangeWatchedFilesParams;
import org.eclipse.lsp4j.services.WorkspaceService;


public class MyWorkspaceService implements WorkspaceService {


  // This could be a setting in your extension's configuration
  private boolean validationEnabled = true;

  @Override
  public void didChangeConfiguration(DidChangeConfigurationParams params) {
    // The settings are sent as a JSON object with the settings as keys.
    // You can use a library such as Gson to parse the settings.
    JsonObject settings = new Gson().fromJson(params.getSettings().toString(), JsonObject.class);
    if (settings.has("drl-extension.validation")) {
      validationEnabled = settings.get("drl-extension.validation").getAsBoolean();
    }
  }

  @Override
  public void didChangeWatchedFiles(DidChangeWatchedFilesParams params) {
  }


  public boolean isValidationEnabled() {
    return validationEnabled;
  }

  // Implement other methods...
}
