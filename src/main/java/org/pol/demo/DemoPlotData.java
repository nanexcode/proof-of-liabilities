package org.pol.demo;

import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.pol.Pol;
import org.pol.crypto.Hash256;
import org.pol.tree.Node;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DemoPlotData {

    private static int totalNodes = 16;
    private static int totalAccounts = 12;
    private static  Hash256 hash256;

    static {
        try {
            hash256 = new Hash256();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        System.setProperty("org.graphstream.ui", "swing");
        Pol facade = generatePol();
        facade.getTree().forEach(n -> System.out.println(n.getHash()));
        showGraph(facade.getTree(), facade.getRootNode());
    }

    private static Pol generatePol() throws NoSuchAlgorithmException {
        /**
         * Generate the merkle tree with balances is as easy as call this method.
         */
        Pol facade = new Pol(totalNodes);
        facade.create(getNodes(totalAccounts));
        return facade;
    }

    private static void showGraph(List<Node> nodes, Node rootNode) {
        Graph graph = new SingleGraph("Proof of Liabilities");
        graph.setAttribute("ui.quality");
        graph.setAttribute("ui.antialias");
        graph.setAttribute("ui.stylesheet", styleSheet);
        //nodes.forEach(n -> graph.addNode(n.getHash()));
        addNodes(rootNode, graph, nodes.size(), 0,nodes.size() / 2, 0, 0);

        nodes.forEach(n -> {
            if (n.getLeftNode() != null && n.getRightNode() != null) {
                graph.addEdge(String.format("%s->%s", n.getHash(), n.getLeftNode().getHash()), n.getHash(), n.getLeftNode().getHash());
                graph.addEdge(String.format("%s->%s", n.getHash(), n.getRightNode().getHash()), n.getHash(), n.getRightNode().getHash());
            }
        });

        Viewer viewer = graph.display();
        viewer.disableAutoLayout();
    }

    private static void addNodes(Node node, Graph graph, double totalNodes, double level, double x, double y, double z) {
        if (node == null) return;

        System.out.println(String.format("Adding node at: %s, %s, %s", x, y, z));

        org.graphstream.graph.Node graphNode = graph.addNode(node.getHash());
        graphNode.setAttribute("xyz", new double[] { x, y, z});
        graphNode.setAttribute("ui.label", String.format("%s... (%s)", node.getHash().substring(0, 4), node.getBalance()));
        level+=2;
        double leftNodeX = x - totalNodes / level;
        double rightNodeX = x + totalNodes / level;

        addNodes(node.getRightNode(), graph, totalNodes, level, rightNodeX, y-5, z);
        addNodes(node.getLeftNode(), graph, totalNodes, level, leftNodeX, y-5, z);
    }

    private static List<Node> getNodes(int size) {
        List<Node> nodes = new ArrayList<>();
        Random random = new Random();
        for (int i=0; i<size; i++) {
            int val = random.nextInt(50);
            nodes.add(createNode(new BigDecimal(val)));
        }
        return nodes;
    }

    private static Node createNode(BigDecimal balance) {
        Node node = new Node();
        node.setBalance(balance);
        node.setHash(hash256.hash(UUID.randomUUID().toString(), balance.toString()));
        return node;
    }

    private static String styleSheet =
            "node {"+
                    "	fill-color: white;"+
                    "	fill-mode: plain;"+
                    "	stroke-mode: plain;"+
                    "	stroke-width: 1px;"+
                    "	stroke-color: red;"+
                    "   text-style: bold;"+
                    "   text-alignment: above;"+
                    "	size: 10px;"+
                    "}"+
                    "node#C {"+
                    "	stroke-mode: double;"+
                    "}"+
                    "node#D {"+
                    "	fill-color: gray; "+
                    "	stroke-mode: plain; " +
                    "	stroke-color: blue; "+
                    "}"+
                    "edge {"+
                    "	fill-mode: none;"+
                    "	size: 0px;"+
                    "	stroke-mode: plain;"+
                    "	stroke-width: 1px;"+
                    "	stroke-color: blue;"+
                    "}";
}

