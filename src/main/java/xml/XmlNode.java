package xml;

import java.util.ArrayList;
import java.util.HashMap;

class XmlNode {
    private String nodeName;
    private String nodeValue;
    private HashMap<String, String> attributes = new HashMap<>();
    private ArrayList<XmlNode> childNodes = new ArrayList<>();

    XmlNode(String nodeName) {
        this.nodeName = nodeName;
    }

    XmlNode(String nodeName, XmlNode parentChild) {
        this.nodeName = nodeName;
        parentChild.appendChild(this);
    }

    XmlNode(String nodeName, String attrName, String attrValue, XmlNode parentChild) {
        this.nodeName = nodeName;
        this.appendAttribute(attrName, attrValue);
        parentChild.appendChild(this);
    }

    XmlNode(String nodeName, String nodeValue) {
        this.nodeName = nodeName;
        this.nodeValue = nodeValue;
    }

    XmlNode getChildNode(String nodeName) {
        return childNodes.stream().
                filter(x -> x.nodeName.equals(nodeName)).
                findFirst().
                orElseThrow();
    }

    XmlNode[] getChildNodes(@SuppressWarnings("SameParameterValue") String nodeName) {
        return childNodes.stream().
                filter(x -> x.nodeName.equals(nodeName)).
                toArray(XmlNode[]::new);
    }

    String getAttribute(@SuppressWarnings("SameParameterValue") String attrName) {
        return this.attributes.get(attrName);
    }

    void appendAttribute(@SuppressWarnings("SameParameterValue") String attrName, String attrValue) {
        this.attributes.put(attrName, attrValue);
    }

    void setValue(String content) {
        this.nodeValue = content;
    }

    void appendChild(XmlNode child) {
        this.childNodes.add(child);
    }

    void visit(XmlNodeVisitor visitor) {
        if (this.childNodes.isEmpty()) {
            visitor.beginNode(this.nodeName, this.nodeValue, this.attributes.entrySet());
        } else {
            visitor.beginNode(this.nodeName, this.attributes.entrySet());
        }
        for (XmlNode childNode : childNodes) {
            childNode.visit(visitor);
        }
        visitor.endNode();
    }

    String getNodeName() {
        return nodeName;
    }

    String getNodeValue() {
        return nodeValue;
    }

}
