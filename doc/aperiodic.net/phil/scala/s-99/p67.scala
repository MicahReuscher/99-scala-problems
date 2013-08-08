// P67 (**) A string representation of binary trees.
//     Somebody represents binary trees as strings of the following type (see
//     example opposite):
//
//     a(b(d,e),c(,f(g,)))
//
//     Write a method which generates this string representation, if the tree
//     is given as usual (in Nodes and Ends).  Use that method for the Tree
//     class's and subclass's toString methods.  Then write a method (on the
//     Tree object) which does this inverse; i.e. given the string
//     representation, construct the tree in the usual form.
//
//     For simplicity, suppose the information in the nodes is a single letter
//     and there are no spaces in the string.
//
//     scala> Node('a', Node('b', Node('d'), Node('e')), Node('c', End, Node('f', Node('g'), End))).toString
//     res0: String = a(b(d,e),c(,f(g,)))
//
//     scala> Tree.fromString("a(b(d,e),c(,f(g,)))")
//     res1: Node[Char] = a(b(d,e),c(,f(g,)))

// TODO: Implement string2Tree with parser combinators.

case class Node[+T](value: T, left: Tree[T], right: Tree[T]) extends Tree[T] {
  override def toString = (left, right) match {
    case (End, End) => value.toString
    case _ => value.toString + "(" + left + "," + right + ")"
  }
}

case class PositionedNode[+T](override val value: T, override val left: Tree[T], override val right: Tree[T], x: Int, y: Int) extends Node[T](value, left, right) {
  override def toString = (left, right) match {
    case (End, End) => value + "[" + x + "," + y + "]"
    case _ => value + "[" + x + "," + y + "](" + left + "," + right + ")"
  }
}

case object End extends Tree[Nothing] {
  override def toString = ""
}

object Tree {
  def string2Tree(s: String): Tree[Char] = {
    def extractTreeString(s: String, start: Int, end: Char): (String,Int) = {
      def updateNesting(nesting: Int, pos: Int): Int = s(pos) match {
        case '(' => nesting + 1
        case ')' => nesting - 1
        case _   => nesting
      }
      def findStringEnd(pos: Int, nesting: Int): Int = 
        if (s(pos) == end && nesting == 0) pos
        else findStringEnd(pos + 1, updateNesting(nesting, pos))
      val strEnd = findStringEnd(start, 0)
      (s.substring(start, strEnd), strEnd)
    }
    s.length match {
      case 0 => End
      case 1 => Node(s(0))
      case _ => {
        val (leftStr, commaPos) = extractTreeString(s, 2, ',')
        val (rightStr, _) = extractTreeString(s, commaPos + 1, ')')
        Node(s(0), string2Tree(leftStr), string2Tree(rightStr))
      }
    }
  }
}
