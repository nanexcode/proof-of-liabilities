package org.pol.tree;

import java.math.BigDecimal;

public class Node {
    private BigDecimal balance;
    private String hash;
    private Node rightNode;
    private Node leftNode;


    public void insertNode(Node node) {
        if (rightNode == null) {
            rightNode = node;
        }
        else if (leftNode == null) {
            leftNode = node;
        }

        if (rightNode != null && leftNode != null) {
            this.balance = rightNode.balance.add(leftNode.balance);
        }
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    public Node getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node leftNode) {
        this.leftNode = leftNode;
    }
}
