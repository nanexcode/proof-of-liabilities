package org.pol;

import org.junit.Before;
import org.junit.Test;
import org.pol.crypto.Hash256;
import org.pol.tree.Node;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests to validate that the root node total balance
 * is equals to the total balance of all the accounts.
 */
public class PolTest {

    private Hash256 hash256;

    @Before
    public void init() throws NoSuchAlgorithmException {
        this.hash256 = new Hash256();
    }

    @Test
    public void testCreateTree1024() throws NoSuchAlgorithmException {
        List<Node> nodes = getNodes(1000);
        BigDecimal balance = getBalance(nodes);

        Pol facade = new Pol(nodes.size());
        facade.create(nodes);
        Node root = facade.getRootNode();

        assertNotNull(root);
        assertEquals(balance.intValue(), root.getBalance().intValue());
    }

    @Test
    public void testCreateTree16m() throws NoSuchAlgorithmException {
        List<Node> nodes = getNodes(5_000_000);
        BigDecimal balance = getBalance(nodes);

        Pol facade = new Pol(16_777_216);
        facade.create(nodes);
        Node root = facade.getRootNode();

        assertNotNull(root);
        assertEquals(balance.intValue(), root.getBalance().intValue());
    }

    @Test
    public void testCreateTree256() throws NoSuchAlgorithmException {
        List<Node> nodes = getNodes(200);
        BigDecimal balance = getBalance(nodes);

        Pol facade = new Pol(nodes.size());
        facade.create(nodes);
        Node root = facade.getRootNode();

        assertNotNull(root);
        assertEquals(balance.intValue(), root.getBalance().intValue());
    }

    private List<Node> getNodes(int size) {
        List<Node> nodes = new ArrayList<>();
        Random random = new Random();
        for (int i=0; i<size; i++) {
            int val = random.nextInt(50);
            nodes.add(createNode(new BigDecimal(val)));
        }
        return nodes;
    }

    private BigDecimal getBalance(List<Node> nodes) {
        return BigDecimal.valueOf(nodes.stream().mapToLong(n -> n.getBalance().longValue()).sum());
    }

    private Node createNode(BigDecimal balance) {
        Node node = new Node();
        node.setBalance(balance);
        node.setHash(hash256.hash(UUID.randomUUID().toString(), balance.toString()));
        return node;
    }
}
