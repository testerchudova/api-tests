<#ftl output_format="HTML">

<h3>Request</h3>

<#if data.method??>
    <div><b>Method:</b> ${data.method}</div>
</#if>

<#if data.url??>
    <div><b>URL:</b> ${data.url}</div>
</#if>

<#if (data.headers)?has_content>
    <h4>Headers</h4>
    <pre><#list data.headers as name, value>${name}: ${value!"null"}
</#list></pre>
</#if>

<#if (data.cookies)?has_content>
    <h4>Cookies</h4>
    <pre><#list data.cookies as name, value>${name}: ${value!"null"}
</#list></pre>
</#if>

<#if (data.formParams)?has_content>
    <h4>Form params</h4>
    <pre><#list data.formParams as name, value>${name}: ${value!"null"}
</#list></pre>
</#if>

<#if data.body??>
    <h4>Body</h4>
    <pre>${data.body}</pre>
</#if>

<#if data.curl??>
    <h4>cURL</h4>
    <pre>${data.curl}</pre>
</#if>