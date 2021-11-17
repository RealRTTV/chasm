package org.quiltmc.chasm.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.TypePath;
import org.quiltmc.chasm.tree.*;

public class ChasmFieldVisitor extends FieldVisitor {

    private final ListNode annotations = new LinkedListNode();
    private final ListNode attributes = new LinkedListNode();

    public ChasmFieldVisitor(int api, MapNode fieldNode) {
        super(api);

        fieldNode.put("annotations", annotations);
        fieldNode.put("attributes", attributes);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        MapNode annotation = new LinkedHashMapNode();
        ListNode values = new LinkedListNode();
        annotation.put("descriptor", new ValueNode<>(descriptor));
        annotation.put("visible", new ValueNode<>(visible));
        annotation.put("values", values);
        annotations.add(annotation);

        return new ChasmAnnotationVisitor(api, values);
    }

    @Override
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        MapNode annotation = new LinkedHashMapNode();
        ListNode values = new LinkedListNode();
        annotation.put("descriptor", new ValueNode<>(descriptor));
        annotation.put("visible", new ValueNode<>(visible));
        annotation.put("values", new ValueNode<>(values));
        annotation.put("typeRef", new ValueNode<>(typeRef));
        annotation.put("typePath", new ValueNode<>(typePath.toString()));
        annotations.add(annotation);

        return new ChasmAnnotationVisitor(api, values);
    }

    @Override
    public void visitAttribute(Attribute attribute) {
        attributes.add(new ValueNode<>(attribute));
    }

    @Override
    public void visitEnd() {
        // Nothing to do here
    }
}