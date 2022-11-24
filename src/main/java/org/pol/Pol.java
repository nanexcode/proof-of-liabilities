package org.pol;

import com.google.common.math.IntMath;
import org.pol.crypto.Hash256;
import org.pol.tree.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Proof of liabilities (pol) is a helper class that allows the user to generate a merkle tree
 * where each node in the tree has a balance that is equals to the balance of the 2 direct childs.
 * The root node balance is equals to the sum of all the base nodes.
 *
 * In order to use this library
 */
public class Pol {
    private static final Logger LOGGER = LoggerFactory.getLogger(Pol.class);
    private int totalNodes = 65536;
    private Node rootNode;
    private final List<Node> tree = new ArrayList<>();
    private final Hash256 hash256;

    public Pol(int totalNodes) throws NoSuchAlgorithmException {
        this.hash256 = new Hash256();
        this.rootNode = new Node();
        this.totalNodes = IntMath.ceilingPowerOfTwo(totalNodes);
    }

    public void create(List<Node> nodes) {
        nodes.addAll(createFakeNodes(totalNodes - nodes.size()));
        shuffle(nodes);
        createTree(nodes);
    }

    public Node getRootNode() {
        return this.rootNode;
    }

    public List<Node> getTree() {
        return this.tree;
    }

    /**
     * Call this method with the list of accounts you want to include in your proof.
     * After this method finish you can use the result rootNode or tree list
     * to navigate over the merkle tree.
     *
     * @param nodes
     */
    private void createTree(List<Node> nodes) {
        LOGGER.info("{} nodes in the current level", nodes.size());
        this.tree.addAll(nodes);
        process(nodes);
    }

    private void process(List<Node> nodes) {
        if (nodes.size() == 2) {
            this.rootNode = buildNode(nodes.get(0), nodes.get(1));
            LOGGER.info(
                    "Root node created with {} and {}, total balance {}",
                    nodes.get(0).getBalance(),
                    nodes.get(1).getBalance(),
                    this.rootNode.getBalance()
            );
        } else {
            final List<Node> levelNodes = new ArrayList<>();
            for (int i=0; i<nodes.size()-1; i+=2) {
                Node node = buildNode(nodes.get(i), nodes.get(i+1));
                levelNodes.add(node);
            }
            LOGGER.info("{} nodes in the current level", levelNodes.size());
            process(levelNodes);
        }
    }

    private Node buildNode(Node leave1, Node leave2) {
        BigDecimal balance = leave1.getBalance().add(leave2.getBalance());

        Node node = new Node();
        node.setBalance(balance);
        node.setLeftNode(leave1);
        node.setRightNode(leave2);
        node.setHash(hash256.hash(UUID.randomUUID().toString(), balance.toString()));

        tree.add(node);

        return node;
    }

    /**
     * Shuffle the nodes so 2 calls to the pol class with the same data will produce different hashes for each node,
     * but it will generate the same balance.
     *
     * @param nodes
     */
    private void shuffle(List<Node> nodes) {
        final Random random = new Random();

        for (int i=0; i<nodes.size(); i++) {
            int j = random.nextInt(nodes.size() - i);
            Node temp = nodes.get(i);
            nodes.set(i, nodes.get(j));
            nodes.set(j, temp);
        }
    }

    private List<Node> createFakeNodes(long size) {
        final List<Node> nodes = new ArrayList<>();
        for (int i=0; i<size; i++) {
            final Node node = new Node();
            node.setBalance(BigDecimal.ZERO);
            node.setHash(hash256.hash(UUID.randomUUID().toString(), BigDecimal.ZERO.toString()));
            nodes.add(node);
        }
        LOGGER.info("{} fake nodes created", size);
        return nodes;
    }
}
