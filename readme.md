# Vehicle Loan Calculator

## Resolving Mockito Agent Warning

When running tests, you might encounter the following warning:

```
Mockito is currently self-attaching to enable the inline-mock-maker. This will no longer work in future releases of the JDK. Please add Mockito as an agent to your build what is described in Mockito's documentation: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3
WARNING: A Java agent has been loaded dynamically (C:\Users\jeppy\.m2\repository\net\bytebuddy\byte-buddy-agent\1.15.11\byte-buddy-agent-1.15.11.jar)
WARNING: If a serviceability tool is in use, please run with -XX:+EnableDynamicAgentLoading to hide this warning
WARNING: If a serviceability tool is not in use, please run with -Djdk.instrument.traceUsage for more information
WARNING: Dynamic loading of agents will be disallowed by default in a future release
Java HotSpot(TM) 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
```

This warning is related to Mockito's inline mock maker and dynamic agent loading in JDK 17 and later versions. To resolve this warning, we have explicitly added the `byte-buddy-agent` as a Java agent in the `pom.xml` file.

### pom.xml Configuration

The following configuration was added to the `maven-surefire-plugin` in `pom.xml` to include the `byte-buddy-agent`:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <configuration>
        <!--
            Adding byte-buddy-agent to resolve Mockito inline mock warning.
            This is a known issue with Mockito and JDK 17+, related to dynamic agent loading.
            Adding this javaagent explicitly to surefire plugin configuration resolves the warning.
            More details can be found in Mockito documentation: https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html#0.3
         -->
        <argLine>-javaagent:C:/Users/jeppy/.m2/repository/net/bytebuddy/byte-buddy-agent/1.15.11/byte-buddy-agent-1.15.11.jar</argLine>
    </configuration>
</plugin>
```

By adding this configuration, the `byte-buddy-agent` is loaded as a Java agent when running tests, which resolves the warning message.

Please ensure you have this configuration in your `pom.xml` to avoid the Mockito agent warning during tests.
