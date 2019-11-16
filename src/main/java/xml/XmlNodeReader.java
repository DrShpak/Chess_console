package xml;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class XmlNodeReader {
    private File file;
    private List<String> lines;
    private ArrayDeque<XmlNode> nodes = new ArrayDeque<>();

    XmlNodeReader(String savePath) {
        this.file = new File(savePath);
    }

    XmlNode load() {
        if (!this.file.exists()) {
            throw new IllegalArgumentException("file " + this.file.getPath() + " does not exist!");
        }
        try {
            this.lines = Files.
                    readAllLines(Paths.get(this.file.getPath())).stream().
                    map(x -> x.replaceFirst("^\\s+", "")).
                    collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        processLines();
        return this.nodes.pop();
    }

    private void processLines() {
        for (String line : this.lines) {
            var startTagMatcher = Pattern.compile("^<?([^<>/]+?)( [^<>/]+?)?>$").matcher(line);
            if (startTagMatcher.matches()) {
                var newNode = new XmlNode(startTagMatcher.group(1));
                parseAttributes(startTagMatcher.group(2)).
                        forEach(x -> newNode.appendAttribute(x.getKey(), x.getValue()));
                if (!this.nodes.isEmpty()) {
                    this.nodes.peek().appendChild(newNode);
                }
                this.nodes.push(newNode);
                continue;
            }
            var endTagMatcher = Pattern.compile("^</.+?>$").matcher(line);
            if (endTagMatcher.matches()) {
                if (this.nodes.size() > 1) {
                    this.nodes.pop();
                }
                continue;
            }
            var emptyTag = Pattern.compile("^<(.+?)/>$").matcher(line);
            if (emptyTag.matches()) {
                var newNode = new XmlNode(emptyTag.group(1));
                assert this.nodes.peek() != null;
                this.nodes.peek().appendChild(newNode);
                continue;
            }
            var defaultTag = Pattern.compile("^<(.+?)( [^<>/]+?)?>(.+?)</\\1>$").matcher(line);
            defaultTag.matches();
            {
                var newNode = new XmlNode(defaultTag.group(1), defaultTag.group(3));
                parseAttributes(defaultTag.group(2)).
                        forEach(x -> newNode.appendAttribute(x.getKey(), x.getValue()));
                assert this.nodes.peek() != null;
                this.nodes.peek().appendChild(newNode);
            }
        }
    }

    private List<Map.Entry<String, String>> parseAttributes(String attrString) {
        return  getAllGroups(
                    Pattern.compile(" (.+?=\".+?\")").
                    matcher(attrString != null ? attrString : "")
                ).
                map(x ->
                        getAllGroups(
                                Pattern.compile("(.+?)=\"(.+?)\"").
                                matcher(x)
                        ).toArray(String[]::new)
                ).
                map(x -> Map.entry(x[0], x[1])).
                collect(Collectors.toList());
    }

    private Stream<String> getAllGroups(Matcher matcher) {
        var groups = new ArrayList<String>();
        if (!matcher.matches()) {
            return Stream.empty();
        }
        for (int i = 1; i <= matcher.groupCount(); i++) {
            groups.add(matcher.group(i));
        }
        return groups.stream();
    }
}
