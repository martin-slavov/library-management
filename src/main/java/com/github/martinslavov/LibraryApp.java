package com.github.martinslavov;

import com.github.martinslavov.commands.CommandProcessor;
import com.github.martinslavov.util.AppContext;

public class LibraryApp {

    public void run() {
        AppContext context = new AppContext();
        context.overdueScanner.start();
        new CommandProcessor(context).process();
        context.overdueScanner.stop();
    }
}