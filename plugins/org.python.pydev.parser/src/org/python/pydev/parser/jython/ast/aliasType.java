// Autogenerated AST node
package org.python.pydev.parser.jython.ast;

import org.python.pydev.parser.jython.SimpleNode;
import java.util.Arrays;

public final class aliasType extends SimpleNode {
    public NameTokType name;
    public NameTokType asname;

    public aliasType(NameTokType name, NameTokType asname) {
        this.name = name;
        this.asname = asname;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((asname == null) ? 0 : asname.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        aliasType other = (aliasType) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (asname == null) {
            if (other.asname != null)
                return false;
        } else if (!asname.equals(other.asname))
            return false;
        return true;
    }

    public aliasType createCopy() {
        return createCopy(true);
    }

    public aliasType createCopy(boolean copyComments) {
        aliasType temp = new aliasType(name != null ? (NameTokType) name.createCopy(copyComments) : null,
                asname != null ? (NameTokType) asname.createCopy(copyComments) : null);
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
        StringBuffer sb = new StringBuffer("alias[");
        sb.append("name=");
        sb.append(dumpThis(this.name));
        sb.append(", ");
        sb.append("asname=");
        sb.append(dumpThis(this.asname));
        sb.append("]");
        return sb.toString();
    }

    public Object accept(VisitorIF visitor) throws Exception {
        traverse(visitor);
        return null;
    }

    public void traverse(VisitorIF visitor) throws Exception {
        if (name != null) {
            name.accept(visitor);
        }
        if (asname != null) {
            asname.accept(visitor);
        }
    }

}
