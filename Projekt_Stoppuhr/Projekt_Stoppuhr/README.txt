Das "Template_Stoppuhr" enthält eine Vorlage zur Lösung der Projektaufgabe.

- In der Konfiguration sind auch Objekte der Klasse "Digital" enthalten, mit denen die LEDS und Buttons verwendet werden können.
- Das Beispiel enthält auch eine Klasse "App" zur Verwendung des Timer-Interrupts mit dem Beobachter-Muster gemäß Vorlesung.
- Die Projektaufgabe kann auch OHNE Controller-Board gelöst werden. Dazu bitte zunächst die IDE "CodeBlocks" herunterladen (siehe "https://sourceforge.net/projects/codeblocks/files/Binaries/20.03/Windows/codeblocks-20.03mingw-setup.exe/download") und installieren.
Anschließend das CodeBlocks-Projekt unter "...\Project\Virtual\CodeBlocks\Virtual.cbp" öffnen.
Das Target "Virtual" emuliert einen Mikrocontroller auf dem Windows-PC mit Hilfe eines Server-Programms. 
Dazu muss vor dem Start der Anwendung die Batch-Datei "_VirtualDeviceServer.bat" gestartet werden.
Das Projekt verwendet die selben Quell-Dateien (main.cpp) und kann jederzeit auch auf Mikrocontroller ausgeführt werden.