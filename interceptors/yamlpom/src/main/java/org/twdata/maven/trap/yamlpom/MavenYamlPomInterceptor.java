package org.twdata.maven.trap.yamlpom;

import org.twdata.maven.interceptor.MavenInterceptor;
import org.twdata.maven.interceptor.EnvMavenInterceptor;
import org.twdata.maven.yamlpom.SyncManager;
import org.twdata.maven.yamlpom.InvalidFormatException;
import org.twdata.maven.yamlpom.Log;
import org.twdata.maven.yamlpom.SysOutLog;
import static org.twdata.maven.yamlpom.ConverterBuilder.convertYamlToXml;
import static org.twdata.maven.yamlpom.ConverterBuilder.convertXmlToYaml;

import java.io.File;
import java.io.IOException;

/**
 *
 */
public class MavenYamlPomInterceptor extends EnvMavenInterceptor {
    private static final int xmlIndent = 4;
    private static final int yamlIndent = 2;

    public MavenYamlPomInterceptor() {


    }

    public String[] onBefore(String[] args) {
        File xmlFile = new File("pom.xml");

        File yamlFile = new File("pom.yml");
        File syncFile = new File(".pom.yml");

        SyncManager syncManager = new SyncManager(xmlFile, yamlFile, syncFile);
        switch (syncManager.determineFormatToTarget())
        {
            case YAML:
                System.out.println("Converting "+xmlFile.getName() + " into " + yamlFile.getName());
                sync(xmlFile, yamlFile, false);
                syncManager.save();
                break;
            case XML:
                System.out.println("Converting "+yamlFile.getName() + " into " + xmlFile.getName());
                sync(xmlFile, yamlFile, true);
                syncManager.save();
                break;
            case SYNC_FILE_ONLY:
                System.out.println("Files in sync, creating a sync file");
                syncManager.save();
                break;
            case NONE:
                System.out.println("No sync required");
                break;
            case UNKNOWN:
                fail("Unable to automatically sync due to changes to both XML and YAML since last sync.");
        }

        return args;
    }

    private final void fail(String msg) {
        System.err.println(msg);
        System.exit(1);
    }

    public void onAfter(int exitCode) {
    }

    protected String getEnvironmentVariableName() {
        return "MAVEN_YAMLPOM";
    }

    private void sync(File xmlFile, File yamlFile, boolean xmlFirst)
    {
        Log log = new SysOutLog();
        try
        {
            if (xmlFirst)
            {
                convertYamlToXml()
                        .indentSpaces(xmlIndent)
                        .fromFile(yamlFile)
                        .toFile(xmlFile)
                        .logWith(log)
                        .convert();
                convertXmlToYaml()
                        .indentSpaces(yamlIndent)
                        .fromFile(xmlFile)
                        .toFile(yamlFile)
                        .logWith(log)
                        .convert();
            }
            else
            {
                convertXmlToYaml()
                        .indentSpaces(yamlIndent)
                        .fromFile(xmlFile)
                        .toFile(yamlFile)
                        .logWith(log)
                        .convert();
                convertYamlToXml()
                        .indentSpaces(xmlIndent)
                        .fromFile(yamlFile)
                        .toFile(xmlFile)
                        .logWith(log)
                        .convert();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
