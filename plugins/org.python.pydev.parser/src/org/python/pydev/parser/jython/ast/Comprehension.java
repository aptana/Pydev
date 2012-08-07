// Autogenerated AST node
package org.python.pydev.parser.jython.ast;

import org.python.pydev.parser.jython.SimpleNode;
import java.util.Arrays;

public final class Comprehension extends comprehensionType {
    public exprType target;
    public exprType iter;
    public exprType[] ifs;

    public Comprehension(exprType target, exprType iter, exprType[] ifs) {
        this.target = target;
        this.iter = iter;
        this.ifs = ifs;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((target == null) ? 0 : target.hashCode());
        result = prime * result + ((iter == null) ? 0 : iter.hashCode());
        result = prime * result + Arrays.hashCode(ifs);
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Comprehension other = (Comprehension) obj;
        if (target == null) {
            if (other.target != null)
                return false;
        } else if (!target.equals(other.target))
            return false;
        if (iter == null) {
            if (other.iter != null)
                return false;
        } else if (!iter.equals(other.iter))
            return false;
        if (!Arrays.equals(ifs, other.ifs))
            return false;
        return true;
    }

    public Comprehension createCopy() {
        return createCopy(true);
    }

    public Comprehension createCopy(boolean copyComments) {
        exprType[] new0;
        if (this.ifs != null) {
            new0 = new exprType[this.ifs.length];
            for (int i = 0; i < this.ifs.length; i++) {
                new0[i] = (exprType) (this.ifs[i] != null ? this.ifs[i].createCopy(copyComments) : null);
            }
        } else {
            new0 = this.ifs;
        }
        Comprehension temp = new Comprehension(target != null ? (exprType) target.createCopy(copyComments) : null,
                iter != null ? (exprType) iter.createCopy(copyComments) : null, new0);
        temp.beginLine = this.beginLine;
        temp.beginColumn = this.beginColumn;
        if (this.specialsBefore != null && copyComments) {
            for (Object o : this.specialsBefore) {
                if (o instanceof commentType) {
                    commentType commentType = (commentType) o;
                    temp.getSpecialsBefore().add(commentType.createCopy(copyComments));
                }
            }
        }
        if (this.specialsAfter != null && copyComments) {
            for (Object o : this.specialsAfter) {
                if (o instanceof commentType) {
                    commentType commentType = (commentType) o;
                    temp.getSpecialsAfter().add(commentType.createCopy(copyComments));
                }
            }
        }
        return temp;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("Comprehension[");
        sb.append("target=");
        sb.append(dumpThis(this.target));
        sb.append(", ");
        sb.append("iter=");
        sb.append(dumpThis(this.iter));
        sb.append(", ");
        sb.append("ifs=");
        sb.append(dumpThis(this.ifs));
        sb.append("]");
        return sb.toString();
    }

    public Object accept(VisitorIF visitor) throws Exception {
        return visitor.visitComprehension(this);
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (target != null) {
            target.accept(visitor);
        }
        if (iter != null) {
            iter.accept(visitor);
        }
        if (ifs != null) {
            for (int i = 0; i < ifs.length; i++) {
                if (ifs[i] != null) {
                    ifs[i].accept(visitor);
                }
            }
        }
    }

}
