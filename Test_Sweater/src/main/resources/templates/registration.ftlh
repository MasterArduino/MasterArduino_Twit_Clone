<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>

<#--    ${message}   ${message?ifExists} если что вернуться к уроку 6- -->
  <@c.page>
    <div class="mb-1">Add new user</div>
    ${message?ifExists}
    <@l.login "/registration" true />
</@c.page>