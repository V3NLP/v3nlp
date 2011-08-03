Jump to: 
<a href="#overview">Overview</a> | 
<a href="#summary">Summary of regular-expression constructs</a> | 
<a href="#examples">Examples</a><br>

<h1>Overview</h1>
Expressions use the standard Java regular expression tools: 
<br>
<br>
<a href="http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html">Pattern Java Docs
</a>
<br>
<br>
<h1>Summary of regular-expression constructs</h1>
The following summary information comes directly from the Sun Java Docs:
<br>
<br>
<table border="0" cellpadding="1" cellspacing="0" summary="Regular expression constructs, and what they match"> 

  <tr align="left"> 
    <th bgcolor="#CCCCFF" align="left" id="construct">Construct
    </th> 
    <th bgcolor="#CCCCFF" align="left" id="matches">Matches
    </th> 
  </tr> 

  <tr>
    <th>&nbsp;
    </th>
  </tr> 
  <tr align="left">
    <th colspan="2" id="characters">Characters
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct characters">
      <i>x
      </i>
    </td> 
    <td headers="matches">The character 
      <i>x
      </i>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\\
      </tt>
    </td> 
    <td headers="matches">The backslash character
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\0
      </tt>
      <i>n
      </i>
    </td> 
    <td headers="matches">The character with octal value 
      <tt>0
      </tt>
      <i>n
      </i> 
      (0&nbsp;
      <tt>&lt;=
      </tt>&nbsp;
      <i>n
      </i>&nbsp;
      <tt>&lt;=
      </tt>&nbsp;7)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\0
      </tt>
      <i>nn
      </i>
    </td> 
    <td headers="matches">The character with octal value 
      <tt>0
      </tt>
      <i>nn
      </i> 
      (0&nbsp;
      <tt>&lt;=
      </tt>&nbsp;
      <i>n
      </i>&nbsp;
      <tt>&lt;=
      </tt>&nbsp;7)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\0
      </tt>
      <i>mnn
      </i>
    </td> 
    <td headers="matches">The character with octal value 
      <tt>0
      </tt>
      <i>mnn
      </i> 
      (0&nbsp;
      <tt>&lt;=
      </tt>&nbsp;
      <i>m
      </i>&nbsp;
      <tt>&lt;=
      </tt>&nbsp;3,
      0&nbsp;
      <tt>&lt;=
      </tt>&nbsp;
      <i>n
      </i>&nbsp;
      <tt>&lt;=
      </tt>&nbsp;7)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\x
      </tt>
      <i>hh
      </i>
    </td> 
    <td headers="matches">The character with hexadecimal&nbsp;value&nbsp;
      <tt>0x
      </tt>
      <i>hh
      </i>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>&#92;u
      </tt>
      <i>hhhh
      </i>
    </td> 
    <td headers="matches">The character with hexadecimal&nbsp;value&nbsp;
      <tt>0x
      </tt>
      <i>hhhh
      </i>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="matches">
      <tt>\t
      </tt>
    </td> 
    <td headers="matches">The tab character (
      <tt>'&#92;u0009'
      </tt>)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\n
      </tt>
    </td> 
    <td headers="matches">The newline (line feed) character (
      <tt>'&#92;u000A'
      </tt>)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\r
      </tt>
    </td> 
    <td headers="matches">The carriage-return character (
      <tt>'&#92;u000D'
      </tt>)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\f
      </tt>
    </td> 
    <td headers="matches">The form-feed character (
      <tt>'&#92;u000C'
      </tt>)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\a
      </tt>
    </td> 
    <td headers="matches">The alert (bell) character (
      <tt>'&#92;u0007'
      </tt>)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\e
      </tt>
    </td> 
    <td headers="matches">The escape character (
      <tt>'&#92;u001B'
      </tt>)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct characters">
      <tt>\c
      </tt>
      <i>x
      </i>
    </td> 
    <td headers="matches">The control character corresponding to 
      <i>x
      </i>
    </td>
  </tr> 

  <tr>
    <th>&nbsp;
    </th>
  </tr> 
  <tr align="left">
    <th colspan="2" id="classes">Character classes
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct classes">
      <tt>[abc]
      </tt>
    </td> 
    <td headers="matches">
      <tt>a
      </tt>, 
      <tt>b
      </tt>, or 
      <tt>c
      </tt> (simple class)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct classes">
      <tt>[^abc]
      </tt>
    </td> 
    <td headers="matches">Any character except 
      <tt>a
      </tt>, 
      <tt>b
      </tt>, or 
      <tt>c
      </tt> (negation)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct classes">
      <tt>[a-zA-Z]
      </tt>
    </td> 
    <td headers="matches">
      <tt>a
      </tt> through 
      <tt>z
      </tt> 
      or 
      <tt>A
      </tt> through 
      <tt>Z
      </tt>, inclusive (range)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct classes">
      <tt>[a-d[m-p]]
      </tt>
    </td> 
    <td headers="matches">
      <tt>a
      </tt> through 
      <tt>d
      </tt>,
      or 
      <tt>m
      </tt> through 
      <tt>p
      </tt>: 
      <tt>[a-dm-p]
      </tt> (union)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct classes">
      <tt>[a-z&&[def]]
      </tt>
    </td> 
    <td headers="matches">
      <tt>d
      </tt>, 
      <tt>e
      </tt>, or 
      <tt>f
      </tt> (intersection)
  </tr> 
  <tr>
    <td valign="top" headers="construct classes">
      <tt>[a-z&&[^bc]]
      </tt>
    </td> 
    <td headers="matches">
      <tt>a
      </tt> through 
      <tt>z
      </tt>,
      except for 
      <tt>b
      </tt> and 
      <tt>c
      </tt>: 
      <tt>[ad-z]
      </tt> (subtraction)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct classes">
      <tt>[a-z&&[^m-p]]
      </tt>
    </td> 
    <td headers="matches">
      <tt>a
      </tt> through 
      <tt>z
      </tt>,
      and not 
      <tt>m
      </tt> through 
      <tt>p
      </tt>: 
      <tt>[a-lq-z]
      </tt>(subtraction)
    </td>
  </tr> 
  <tr>
    <th>&nbsp;
    </th>
  </tr> 

  <tr align="left">
    <th colspan="2" id="predef">Predefined character classes
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct predef">
      <tt>.
      </tt>
    </td> 
    <td headers="matches">Any character (may or may not match 
      <a href="#lt">line terminators
      </a>)
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct predef">
      <tt>\d
      </tt>
    </td> 
    <td headers="matches">A digit: 
      <tt>[0-9]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct predef">
      <tt>\D
      </tt>
    </td> 
    <td headers="matches">A non-digit: 
      <tt>[^0-9]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct predef">
      <tt>\s
      </tt>
    </td> 
    <td headers="matches">A whitespace character: 
      <tt>[ \t\n\x0B\f\r]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct predef">
      <tt>\S
      </tt>
    </td> 
    <td headers="matches">A non-whitespace character: 
      <tt>[^\s]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct predef">
      <tt>\w
      </tt>
    </td> 
    <td headers="matches">A word character: 
      <tt>[a-zA-Z_0-9]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct predef">
      <tt>\W
      </tt>
    </td> 
    <td headers="matches">A non-word character: 
      <tt>[^\w]
      </tt>
    </td>
  </tr> 

  <tr>
    <th>&nbsp;
    </th>
  </tr> 
  <tr align="left">
    <th colspan="2" id="posix">POSIX character classes
    <b> (US-ASCII only)
    </b>
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Lower}
      </tt>
    </td> 
    <td headers="matches">A lower-case alphabetic character: 
      <tt>[a-z]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Upper}
      </tt>
    </td> 
    <td headers="matches">An upper-case alphabetic character:
      <tt>[A-Z]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{ASCII}
      </tt>
    </td> 
    <td headers="matches">All ASCII:
      <tt>[\x00-\x7F]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Alpha}
      </tt>
    </td> 
    <td headers="matches">An alphabetic character:
      <tt>[\p{Lower}\p{Upper}]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Digit}
      </tt>
    </td> 
    <td headers="matches">A decimal digit: 
      <tt>[0-9]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Alnum}
      </tt>
    </td> 
    <td headers="matches">An alphanumeric character:
      <tt>[\p{Alpha}\p{Digit}]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Punct}
      </tt>
    </td> 
    <td headers="matches">Punctuation: One of 
      <tt>!"#$%&'()*+,-./:;
        <=>?@[\]^_`{|}~
        </tt>
    </td>
  </tr> 
  <!-- <tt>[\!"#\$%&'\(\)\*\+,\-\./:;\<=\>\?@\[\\\]\^_`\{\|\}~]</tt>
          <tt>[\X21-\X2F\X31-\X40\X5B-\X60\X7B-\X7E]</tt> --> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Graph}
      </tt>
    </td> 
    <td headers="matches">A visible character: 
      <tt>[\p{Alnum}\p{Punct}]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Print}
      </tt>
    </td> 
    <td headers="matches">A printable character: 
      <tt>[\p{Graph}\x20]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Blank}
      </tt>
    </td> 
    <td headers="matches">A space or a tab: 
      <tt>[ \t]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Cntrl}
      </tt>
    </td> 
    <td headers="matches">A control character: 
      <tt>[\x00-\x1F\x7F]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{XDigit}
      </tt>
    </td> 
    <td headers="matches">A hexadecimal digit: 
      <tt>[0-9a-fA-F]
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct posix">
      <tt>\p{Space}
      </tt>
    </td> 
    <td headers="matches">A whitespace character: 
      <tt>[ \t\n\x0B\f\r]
      </tt>
    </td>
  </tr> 
  <tr>
    <th>&nbsp;
    </th>
  </tr> 
  <tr align="left">
    <th colspan="2" id="bounds">Boundary matchers
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct bounds">
      <tt>^
      </tt>
    </td> 
    <td headers="matches">The beginning of a line
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct bounds">
      <tt>$
      </tt>
    </td> 
    <td headers="matches">The end of a line
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct bounds">
      <tt>\b
      </tt>
    </td> 
    <td headers="matches">A word boundary
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct bounds">
      <tt>\B
      </tt>
    </td> 
    <td headers="matches">A non-word boundary
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct bounds">
      <tt>\A
      </tt>
    </td> 
    <td headers="matches">The beginning of the input
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct bounds">
      <tt>\G
      </tt>
    </td> 
    <td headers="matches">The end of the previous match
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct bounds">
      <tt>\Z
      </tt>
    </td> 
    <td headers="matches">The end of the input but for the final
      <a href="#lt">terminator
      </a>, if&nbsp;any
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct bounds">
      <tt>\z
      </tt>
    </td> 
    <td headers="matches">The end of the input
    </td>
  </tr> 

  <tr>
    <th>&nbsp;
    </th>
  </tr> 
  <tr align="left">
    <th colspan="2" id="greedy">Greedy quantifiers
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct greedy">
      <i>X
      </i>
      <tt>?
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, once or not at all
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct greedy">
      <i>X
      </i>
      <tt>*
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, zero or more times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct greedy">
      <i>X
      </i>
      <tt>+
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, one or more times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct greedy">
      <i>X
      </i>
      <tt>{
      </tt>
      <i>n
      </i>
      <tt>}
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, exactly 
      <i>n
      </i> times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct greedy">
      <i>X
      </i>
      <tt>{
      </tt>
      <i>n
      </i>
      <tt>,}
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, at least 
      <i>n
      </i> times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct greedy">
      <i>X
      </i>
      <tt>{
      </tt>
      <i>n
      </i>
      <tt>,
      </tt>
      <i>m
      </i>
      <tt>}
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, at least 
      <i>n
      </i> but not more than 
      <i>m
      </i> times
    </td>
  </tr> 

  <tr>
    <th>&nbsp;
    </th>
  </tr> 
  <tr align="left">
    <th colspan="2" id="reluc">Reluctant quantifiers
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct reluc">
      <i>X
      </i>
      <tt>??
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, once or not at all
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct reluc">
      <i>X
      </i>
      <tt>*?
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, zero or more times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct reluc">
      <i>X
      </i>
      <tt>+?
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, one or more times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct reluc">
      <i>X
      </i>
      <tt>{
      </tt>
      <i>n
      </i>
      <tt>}?
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, exactly 
      <i>n
      </i> times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct reluc">
      <i>X
      </i>
      <tt>{
      </tt>
      <i>n
      </i>
      <tt>,}?
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, at least 
      <i>n
      </i> times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct reluc">
      <i>X
      </i>
      <tt>{
      </tt>
      <i>n
      </i>
      <tt>,
      </tt>
      <i>m
      </i>
      <tt>}?
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, at least 
      <i>n
      </i> but not more than 
      <i>m
      </i> times
    </td>
  </tr> 

  <tr>
    <th>&nbsp;
    </th>
  </tr> 
  <tr align="left">
    <th colspan="2" id="poss">Possessive quantifiers
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct poss">
      <i>X
      </i>
      <tt>?+
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, once or not at all
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct poss">
      <i>X
      </i>
      <tt>*+
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, zero or more times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct poss">
      <i>X
      </i>
      <tt>++
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, one or more times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct poss">
      <i>X
      </i>
      <tt>{
      </tt>
      <i>n
      </i>
      <tt>}+
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, exactly 
      <i>n
      </i> times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct poss">
      <i>X
      </i>
      <tt>{
      </tt>
      <i>n
      </i>
      <tt>,}+
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, at least 
      <i>n
      </i> times
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct poss">
      <i>X
      </i>
      <tt>{
      </tt>
      <i>n
      </i>
      <tt>,
      </tt>
      <i>m
      </i>
      <tt>}+
      </tt>
    </td> 
    <td headers="matches">
      <i>X
      </i>, at least 
      <i>n
      </i> but not more than 
      <i>m
      </i> times
    </td>
  </tr> 

  <tr>
    <th>&nbsp;
    </th>
  </tr> 
  <tr align="left">
    <th colspan="2" id="logical">Logical operators
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct logical">
      <i>XY
      </i>
    </td> 
    <td headers="matches">
      <i>X
      </i> followed by 
      <i>Y
      </i>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct logical">
      <i>X
      </i>
      <tt>|
      </tt>
      <i>Y
      </i>
    </td> 
    <td headers="matches">Either 
      <i>X
      </i> or 
      <i>Y
      </i>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct logical">
      <tt>(
      </tt>
      <i>X
      </i>
      <tt>)
      </tt>
    </td> 
    <td headers="matches">X, as a 
      <a href="#cg">capturing group
      </a>
    </td>
  </tr> 

  <tr>
    <th>&nbsp;
    </th>
  </tr> 
  <tr align="left">
    <th colspan="2" id="quot">Quotation
    </th>
  </tr> 

  <tr>
    <td valign="top" headers="construct quot">
      <tt>\
      </tt>
    </td> 
    <td headers="matches">Nothing, but quotes the following character
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct quot">
      <tt>\Q
      </tt>
    </td> 
    <td headers="matches">Nothing, but quotes all characters until 
      <tt>\E
      </tt>
    </td>
  </tr> 
  <tr>
    <td valign="top" headers="construct quot">
      <tt>\E
      </tt>
    </td> 
    <td headers="matches">Nothing, but ends quoting started by 
      <tt>\Q
      </tt>
    </td>
  </tr> 
  <!-- Metachars: !$()*+.<>?[\]^{|} --> 

</table> 
<h1>Examples</h1>
