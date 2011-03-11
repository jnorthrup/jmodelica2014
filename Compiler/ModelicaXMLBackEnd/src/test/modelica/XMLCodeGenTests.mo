/*
    Copyright (C) 2009 Modelon AB

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, version 3 of the License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package XMLCodeGenTests



model RemoveCopyright
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="RemoveCopyright",
         description="Test that licence tag is filtered out",
         template="<!-- test copyright blurb --> test",
         generatedCode="test"
)})));
end RemoveCopyright;

model XMLExtFuncAnnotations1
	annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
		JModelica.UnitTesting.XMLCodeGenTestCase(
			name="XMLExtFuncAnnotations1",
			description="Test that external function annotations are generated correctly with file://.",
			template="$XML_vendorAnnotations$",
			generatedCode="
<VendorAnnotations>
		<Tool name=\"JModelica\">
			<Annotation name=\"Library\" value=\"external1\"/>
			<Annotation name=\"Include\" value=\"#include &quot;external1.h&quot;\"/>
			<Annotation name=\"LibraryDirectory\" value=\"/Library\"/>
			<Annotation name= \"IncludeDirectory\" value=\"/Include\"/>
		</Tool>
	</VendorAnnotations>
"
   )})));
	function extFunc
		external "C" annotation(Library="external1", Include="#include \"external1.h\"",
								LibraryDirectory="file:///Library", IncludeDirectory="file:///Include");
	end extFunc;
	
algorithm
	extFunc();
	
end XMLExtFuncAnnotations1;

model XMLExtFuncAnnotations2
	annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
		JModelica.UnitTesting.XMLCodeGenTestCase(
			name="XMLExtFuncAnnotations2",
			description="Test that external function annotations are generated correctly without explicit Include or Library directories.",
			template="$XML_vendorAnnotations$",
			filter=true,
			generatedCode="
<VendorAnnotations>
		<Tool name=\"JModelica\">
			<Annotation name=\"Library\" value=\"external1\"/>
			<Annotation name=\"Include\" value=\"#include &quot;external1.h&quot;\"/>
			<Annotation name=\"LibraryDirectory\" value=\"%dir%/Resources/Library\"/>
			<Annotation name= \"IncludeDirectory\" value=\"%dir%/Resources/Include\"/>
		</Tool>
	</VendorAnnotations>
"
   )})));
	function extFunc
		external "C" annotation(Library="external1", Include="#include \"external1.h\"");
	end extFunc;
	
algorithm
	extFunc();
	
end XMLExtFuncAnnotations2;


  model XMLCodeGenTest1
  	parameter Real rp1=1;
  	parameter Real rp2=rp1;
    parameter Real rp3(start=1);
    parameter Real rp4(start=rp1);
    parameter Real rp5(start=rp1) = 5;
    Real r1(start=1);
    Real r2=3;
    Real r3;
	input Real r4 = 5;

  	parameter Integer ip1=1;
  	parameter Integer ip2=ip1;
    parameter Integer ip3(start=1);
    parameter Integer ip4(start=ip1);
    parameter Integer ip5(start=ip1) = 5;
    Integer i1(start=1);
    Integer i2=3;
  	Integer i3=4;
	input Integer r4 = 5;

  	parameter Boolean bp1=true;
  	parameter Boolean bp2=bp1;
    parameter Boolean bp3(start=true);
    parameter Boolean bp4(start=bp1);
    parameter Boolean bp5(start=bp1) = false;
    Boolean b1(start=true);
    Boolean b2=true;
	Boolean b3=true;
	input Boolean b4 =true;
		
  	parameter String sp1="hello";
  	parameter String sp2=sp1;
    parameter String sp3(start="hello");
    parameter String sp4(start=sp1);
    parameter String sp5(start=sp1) = "hello";
    String s1(start="hello");
    String s2="hello";
	String s3="hello";
	input String s4="hello";
    
    equation 
     r1 = 1;
     der(r3)=1;
     i1 = 1;
     b1 = true;
     s2 = "hello";
  end XMLCodeGenTest1;

model XMLCodeGenTest2
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="XMLCodeGenTest2",
         description="Start attribute of independent parameters.",
         template="$XML_variables$",
         generatedCode="
<ScalarVariable name=\"b1\" valueReference=\"536870916\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
<Boolean start=\"true\" />
<isLinear>true</isLinear>
<VariableCategory>independentParameter</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"b2\" valueReference=\"536870917\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
<Boolean start=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>independentParameter</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"i1\" valueReference=\"268435458\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
<Integer start=\"1\" />
<isLinear>true</isLinear>
<VariableCategory>independentParameter</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"i2\" valueReference=\"268435459\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
<Integer />
<isLinear>true</isLinear>
<VariableCategory>dependentParameter</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"p1\" valueReference=\"0\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" start=\"5.0\" />
<isLinear>true</isLinear>
<VariableCategory>independentParameter</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"p2\" valueReference=\"1\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>dependentParameter</VariableCategory>
</ScalarVariable>
")})));

	parameter Real p1 = 5;
	parameter Real p2 = p1+4;
	parameter Integer i1 = 1;
	parameter Integer i2 = i1+1;
	parameter Boolean b1 = true;
	parameter Boolean b2 = false;
	equation		
end XMLCodeGenTest2;

	model XMLCodeGenTest3	
		parameter Real p1 = 5;
		parameter Real p2 = p1+4;
		parameter Real p3 = p2-1;
		parameter Real p4 = p3*6;
		parameter Real p5 = p4/6;
		parameter Real p6 = -p5;
		parameter Real p7 = p6/83;
		parameter Real p8 = p7^2;
   	 equation
   	end XMLCodeGenTest3;

	model XMLCodeGenTest4	
		Real r(quantity="q", unit="kg", displayUnit="g", 
		  min=-2-1,max=4,start=1+1,fixed=false,nominal=4-3) = 1;
		Integer i(quantity="q", min=-2-1,max=4,start=1+1,fixed=false) = 1;
		Boolean b(quantity="q", start=true,fixed=false) = 1;
		String s(quantity="q",start="qwe") = 1;


   	 equation
   	end XMLCodeGenTest4;

	model XMLCodeGenTest5
	
		constant Real r1(unit="m", displayUnit="mm",
			min = 2*0, max = 2^5, start = 10, nominal = 0) = 1;
		parameter Real r2(quantity="q1", min = 0, max = 10, start = 0) = 2;
		discrete Real r3(quantity="q1", unit = "Hz", max = 100) = 3;
		input Real r4(start = 5, min=-10);
		output Real r5(max=4, min=3) = 1;
		
		protected Integer i1(start=5)=2;
		Integer i2(quantity="g",max=1000)=3;
		Integer i3(min=1);
		
		String s1;
		String s2(start="abc")="def";
		
		equation
			s1 = "ghi";
			r5 = der(r4);	
		
	end XMLCodeGenTest5;	

	
	model EnumerationTest1
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="EnumerationTest1",
         description="Test that TypeDefinitions section is generated correctly.",
         template="$XML_typeDefinitions$",
         generatedCode="
<TypeDefinitions>
		<Type name=\"XMLCodeGenTests.EnumerationTest1.A\" description=\"This is A\">
			<EnumerationType min=\"1\" max=\"3\">
				<Item name=\"a\" description=\"This is a\"/>
				<Item name=\"b\"/>
				<Item name=\"c\"/>
			</EnumerationType>
		</Type>
		<Type name=\"XMLCodeGenTests.EnumerationTest1.B\" >
			<EnumerationType min=\"1\" max=\"3\">
				<Item name=\"a\"/>
				<Item name=\"c\"/>
				<Item name=\"b\" description=\"This is b\"/>
			</EnumerationType>
		</Type>
	</TypeDefinitions>")})));

		type A = enumeration(a "This is a", b, c) "This is A";
		type B = enumeration(a, c, b "This is b");
		
		parameter A x = A.a;
		parameter B y = B.b;

	end EnumerationTest1;
	
	model EnumerationTest2
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="EnumerationTest2",
         description="",
         template="$XML_variables$",
         generatedCode="

		<ScalarVariable name=\"x\" valueReference=\"268435456\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Enumeration declaredType=\"XMLCodeGenTests.EnumerationTest2.A\" start=\"1\"/>
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"y\" valueReference=\"268435457\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Enumeration declaredType=\"XMLCodeGenTests.EnumerationTest2.B\" start=\"3\"/>
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>")})));

		type A = enumeration(a "This is a", b, c) "This is A";
		type B = enumeration(a, c, b "This is b");
		
		parameter A x = A.a;
		parameter B y = B.b;

	end EnumerationTest2;
	
	model EnumerationTest3		
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="EnumerationTest3",
         description="",
         template="$XML_variables$",
         generatedCode="

		<ScalarVariable name=\"x\" valueReference=\"268435456\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Enumeration declaredType=\"XMLCodeGenTests.EnumerationTest3.A\" quantity=\"a\" min=\"1\" max=\"3\" start=\"1\"/>
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"y\" valueReference=\"268435457\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Enumeration declaredType=\"XMLCodeGenTests.EnumerationTest3.B\" start=\"1\" fixed=\"true\"/>
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>")})));

		type A = enumeration(a "This is a", b, c) "This is A";
		type B = enumeration(a, c, b "This is b");
		
		parameter A x(quantity="a", min=A.a, max=A.c);
		parameter B y(fixed=true);
		
	end EnumerationTest3;
	
	model EnumerationTest4
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="EnumerationTest4",
         description="",
         template="$XML_variables$",
         generatedCode="

		<ScalarVariable name=\"c\" valueReference=\"268435456\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Enumeration declaredType=\"XMLCodeGenTests.EnumerationTest4.DigitalCurrentChoices\" start=\"2\" fixed=\"true\"/>
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>")})));

		type DigitalCurrentChoices = enumeration(zero, one);

		parameter DigitalCurrentChoices c(start = DigitalCurrentChoices.one, fixed = true);

	end EnumerationTest4;
	
	//model EnumerationTest5
		//type DigitalCurrentChoices = enumeration(zero, one);
		//type DigitalCurrent = DigitalCurrentChoices(quantity="Current",start = DigitalCurrentChoices.one, fixed = true);
		//DigitalCurrent c(start = DigitalCurrent.one, fixed = true);
		//DigitalCurrentChoices c(start = DigitalCurrentChoices.one, fixed = true);

	//end EnumerationTest5;
	
	model VariableSortingTest1	
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="VariableSortingTest1",
         description="",
         template="$XML_variables$",
         generatedCode="

		<ScalarVariable name=\"a\" valueReference=\"1\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"5.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"b\" valueReference=\"3\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"4.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"c\" valueReference=\"2\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"6.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"d\" valueReference=\"0\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"1.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>")})));

		parameter Real d = 1;
		parameter Real a = 5;
		parameter Real c = 6;
		parameter Real b = 4;
   	 equation
		
	end VariableSortingTest1;
	
	model VariableSortingTest2
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="VariableSortingTest2",
         description="",
         template="$XML_variables$",
         generatedCode="

		<ScalarVariable name=\"a1\" valueReference=\"1\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"1.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"a2\" valueReference=\"2\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"2.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"a3\" valueReference=\"0\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"3.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>")})));

		parameter Real a3 = 3;
		parameter Real a1 = 1;
		parameter Real a2 = 2;
		equation
	end VariableSortingTest2;
	
	model VariableSortingTest3
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="VariableSortingTest3",
         description="",
         template="$XML_variables$",
         generatedCode="

		<ScalarVariable name=\"a1\" valueReference=\"2\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"1.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"a11\" valueReference=\"0\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"2.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"a111\" valueReference=\"1\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"3.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"a1111\" valueReference=\"3\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"4.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>")})));

		parameter Real a11 = 2;
		parameter Real a111 = 3;
		parameter Real a1 = 1;
		parameter Real a1111 = 4;
		equation 
	end VariableSortingTest3;
	
	model VariableSortingTest4
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="VariableSortingTest4",
         description="",
         template="$XML_variables$",
         generatedCode="

		<ScalarVariable name=\"a.a\" valueReference=\"1\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"1.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"a.b\" valueReference=\"0\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"2.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"b.a\" valueReference=\"3\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"1.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>
		<ScalarVariable name=\"b.b\" valueReference=\"2\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
			<Real relativeQuantity=\"false\" start=\"2.0\" />
			<isLinear>true</isLinear>
			<VariableCategory>independentParameter</VariableCategory>
		</ScalarVariable>")})));

		model A
			parameter Real b = 2;
			parameter Real a = 1;
			equation
		end A;
		
		model B
			parameter Real b = 2;
			parameter Real a = 1;
			equation
		end B;
		
		A a;
		B b;
		
	end VariableSortingTest4;
	
	model VariableSortingTest5
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="VariableSortingTest5",
         description="",
         template="$XML_variables$",
         generatedCode="
<ScalarVariable name=\"x[1]\" valueReference=\"0\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[2]\" valueReference=\"1\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[3]\" valueReference=\"2\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[4]\" valueReference=\"3\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[5]\" valueReference=\"4\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[6]\" valueReference=\"5\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[7]\" valueReference=\"6\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[8]\" valueReference=\"7\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[9]\" valueReference=\"8\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[10]\" valueReference=\"9\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[11]\" valueReference=\"10\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
")})));

    	Real x[11] = fill(1, 11);
  		equation
    		
	end VariableSortingTest5;
	
	model VariableSortingTest6
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="VariableSortingTest6",
         description="",
         template="$XML_variables$",
         generatedCode="
<ScalarVariable name=\"a\" valueReference=\"0\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" start=\"1.0\" />
<isLinear>true</isLinear>
<VariableCategory>independentParameter</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"b\" valueReference=\"1\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" start=\"2.0\" />
<isLinear>true</isLinear>
<VariableCategory>independentParameter</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[1]\" valueReference=\"8\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>state</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"der(x[1])\" valueReference=\"4\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>derivative</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"x[2]\" valueReference=\"9\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>state</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"der(x[2])\" valueReference=\"5\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>derivative</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"y[1]\" valueReference=\"10\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>state</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"der(y[1])\" valueReference=\"6\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>derivative</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"y[2]\" valueReference=\"7\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>state</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"der(y[2])\" valueReference=\"3\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>derivative</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"z\" valueReference=\"2\" variability=\"parameter\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" start=\"5.0\" />
<isLinear>true</isLinear>
<VariableCategory>independentParameter</VariableCategory>
</ScalarVariable>
")})));

		parameter Real a = 1;
		parameter Real b = 2;
		parameter Real z = 5;
	    Real x[2];
	    Real y[2];
  		equation
  			der(y[2]) = 2;
    		der(x[1]) = 3;
    		der(x[2]) = 4;
    		der(y[1]) = 1;
	end VariableSortingTest6;
	
	model VariableSortingTest7
 annotation(JModelica(unitTesting = JModelica.UnitTesting(testCase={
     JModelica.UnitTesting.XMLCodeGenTestCase(
         name="VariableSortingTest7",
         description="",
         template="$XML_variables$",
         generatedCode="
<ScalarVariable name=\"a.b.c.y[1]\" valueReference=\"0\" variability=\"continuous\" causality=\"internal\" alias=\"alias\" >
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"a.b.c.y[2]\" valueReference=\"1\" variability=\"continuous\" causality=\"internal\" alias=\"alias\" >
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"a.b.x[1,1]\" valueReference=\"0\" variability=\"continuous\" causality=\"internal\" alias=\"alias\" >
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"a.b.x[1,2]\" valueReference=\"2\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"a.b.x[2,1]\" valueReference=\"1\" variability=\"continuous\" causality=\"internal\" alias=\"alias\" >
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"a.b.x[2,2]\" valueReference=\"3\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"a.r[1]\" valueReference=\"0\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
<ScalarVariable name=\"a.r[2]\" valueReference=\"1\" variability=\"continuous\" causality=\"internal\" alias=\"noAlias\">
<Real relativeQuantity=\"false\" />
<isLinear>true</isLinear>
<VariableCategory>algebraic</VariableCategory>
</ScalarVariable>
")})));

		class A
			Real r[2];
 			class B 
 				Real x[2,2];
				class C
		  			Real y[2];
		  			equation
		  				y[1]=2;
		  				y[2]=3;
				end C;
		  		C c;
 			end B;
 			B b;
 			equation
 				b.x[1,1] = b.c.y[1];
    			b.x[1,2] = 2;
    			b.x[2,1] = b.c.y[2];
    			b.x[2,2] = 4;
		end A;
		A a;
		equation
			a.r[1] = a.b.x[1,1];
			a.r[2] = a.b.x[2,1];
	end VariableSortingTest7;
		
end XMLCodeGenTests;
