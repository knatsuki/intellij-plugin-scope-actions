# intellij-plugin-scope-actions

![Build](https://github.com/mukatalab/intellij-plugin-scope-actions/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/PLUGIN_ID.svg)](https://plugins.jetbrains.com/plugin/PLUGIN_ID)

<!-- Plugin description -->
Simple plugin to provide quick shortcuts for scope-specific actions.

- Finding usage by scope
- Showing usage by scope

Intellij provides nice functionality for finding and showing usages by custom search scope. However, the user interrface
to use it is quite inconvenient because you need to manually select the scope using the UI dropdown in the search
action dialog each time you want to search within a specific scope.

This plugin provides the ability to define custom actions and shortcuts for finding and showing usages for any
user-defined search scope.
<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "Scope
  Actions"</kbd> >
  <kbd>Install Plugin</kbd>

- Manually:

  Download the [latest release](https://github.com/mukatalab/intellij-plugin-scope-actions/releases/latest) and install
  it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
