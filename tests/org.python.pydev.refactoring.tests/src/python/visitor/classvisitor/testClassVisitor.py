class Rectangle(object):

    class Foo:
        def foo(self):
            class bla:
                print "foo"
            b = bla()
            print "foo nested class"

    a = Foo() 
    a.foo()
    def __init__(self, width, height):
        self.width = width
        self.height = height
        self.color = "red"
        local_border = "bold"
    def get_area(self):
        return self.width * self.height
   
    area = property(get_area, doc='area of the rectangle')
    
rect = Rectangle(10, 15)
print rect.width
print rect.area

##r

# 3
# Rectangle false
# Foo true
# bla true