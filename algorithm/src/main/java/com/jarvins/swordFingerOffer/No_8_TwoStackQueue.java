package com.jarvins.swordFingerOffer;

import java.util.Stack;

public class No_8_TwoStackQueue {
    Stack<Integer> out;
    Stack<Integer> in;

    public No_8_TwoStackQueue() {
        out = new Stack<>();
        in = new Stack<>();
    }

    public void appendTail(int value) {
        in.push(value);
    }

    public int deleteHead() {
        if (!out.isEmpty()) {
            return out.pop();
        } else {
            if (!in.isEmpty()) {
                while (!in.isEmpty()) {
                    out.push(in.pop());
                }
                return out.pop();
            }
            return -1;
        }
    }
}
