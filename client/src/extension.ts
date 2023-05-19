/* --------------------------------------------------------------------------------------------
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for license information.
 * ------------------------------------------------------------------------------------------ */

import * as path from 'path';
import { workspace, ExtensionContext } from 'vscode';

import {
	LanguageClient,
	LanguageClientOptions,
	ServerOptions,
	TransportKind
} from 'vscode-languageclient/node';

let client: LanguageClient;

export function activate(context: ExtensionContext) {
	// The server is implemented in node
	const path = require('path');
	let workspacePath = path.normalize(__dirname + '\\..\\..');
	let serverExe = workspacePath + '\\server\\drl-server\\build\\launch4j\\server.exe';

	// If the extension is launched in debug mode then the debug server options are used
	// Otherwise the run options are used
	const serverOptions = {
		command: serverExe,
		options: {
			cwd: workspacePath
		}
	};

	// Options to control the language client
	const clientOptions: LanguageClientOptions = {
		// Register the server for plain text documents
		documentSelector: [{ language: 'drl' }],
		synchronize: {
			configurationSection: 'drl',
			fileEvents: workspace.createFileSystemWatcher('**/*.drl')
		}
	};

	// Create the language client and start the client.
	client = new LanguageClient(
		'languageServerExample',
		'Language Server Example',
		serverOptions,
		clientOptions
	);

	// Start the client. This will also launch the server
	client.start();
}

export function deactivate(): Thenable<void> | undefined {
	if (!client) {
		return undefined;
	}
	return client.stop();
}
