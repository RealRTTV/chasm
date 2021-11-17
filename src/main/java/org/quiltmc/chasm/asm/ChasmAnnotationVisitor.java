package org.quiltmc.chasm.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.quiltmc.chasm.tree.*;

public class ChasmAnnotationVisitor extends AnnotationVisitor {
    private final ListNode values;

    public ChasmAnnotationVisitor(int api, ListNode values) {
        super(api);

        this.values = values;
    }

    @Override
    public void visit(String name, Object value) {
        if (value instanceof Object[] array) {
            AnnotationVisitor visitor = visitArray(name);
            for (Object entry : array) {
                visitor.visit(null, entry);
            }
            visitor.visitEnd();
        }
        else if (name == null) {
            this.values.add(new ValueNode<>(value));
        }
        else {
            MapNode valueNode = new LinkedHashMapNode();
            valueNode.put("name", new ValueNode<>(name));
            valueNode.put("value", new ValueNode<>(value));
            this.values.add(valueNode);
        }
    }

    @Override
    public void visitEnum(String name, String descriptor, String value) {
        MapNode enumValueNode = new LinkedHashMapNode();
        enumValueNode.put("descriptor", new ValueNode<>(descriptor));
        enumValueNode.put("value", new ValueNode<>(value));

        if (name != null) {
            MapNode valueNode = new LinkedHashMapNode();
            valueNode.put("name", new ValueNode<>(name));
            valueNode.put("value", enumValueNode);
            this.values.add(valueNode);
        }
        else {
            this.values.add(enumValueNode);
        }
    }

    @Override
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        MapNode annotationValueNode = new LinkedHashMapNode();
        ListNode values = new LinkedListNode();
        annotationValueNode.put("descriptor", new ValueNode<>(descriptor));
        annotationValueNode.put("values", values);

        if (name != null) {
            MapNode valueNode = new LinkedHashMapNode();
            valueNode.put("name", new ValueNode<>(name));
            valueNode.put("value", annotationValueNode);
            this.values.add(valueNode);
        }
        else {
            this.values.add(annotationValueNode);
        }

        return new ChasmAnnotationVisitor(api, values);
    }

    @Override
    public AnnotationVisitor visitArray(String name) {
        ListNode values = new LinkedListNode();

        if (name != null) {
            MapNode valueNode = new LinkedHashMapNode();
            valueNode.put("name", new ValueNode<>(name));
            valueNode.put("value", values);
            this.values.add(valueNode);
        }
        else {
            this.values.add(values);
        }

        return new ChasmAnnotationVisitor(api, values);
    }

    @Override
    public void visitEnd() {
        // Nothing to do
    }
}
