<html>
<head>
      <title>Events From Google Calendar</title>
</head>
<body>
<h2>These Events are Fetched From Your Google Calendar</h2>
<table border="1px;">
      <tr>
            <td>S.N</td><td>Event Description</td><td>Start Date</td><td>End Date</td>
      </tr>

      <g:each var="items" in="${session.items}" status="i">
            <tr><td>${i}</td><td>${items.summary}</td><td>${items.start.dateTime?:items.start.date}</td><td>${items.start.dateTime?:items.start.date}</td></tr>
      </g:each>
</table>
</body>
</html>