<#if bidRequests?size &gt; 0 >
    <#list bidRequests as data>
    <tr>
        <td>${data.createUser.username }</td>
        <td>${data.title}</td>
        <td class="text-info">
        ${data.currentRate}%
        </td>
        <td class="text-info">${data.bidRequestAmount}</td>
        <td>${data.returnTypeDisplay}</td>
        <td>${data.monthes2Return}月</td>
        <td>
            <div class="">
            ${data.persent} %
            </div>
        </td>
        <td><a class="btn btn-danger btn-sm" href="${(data.staticUrl)!'/borrow_info.do?id=${data.id}'}">查看</a></td>
    </tr>
    </#list>
<#else>
<tr>
    <td colspan="7" align="center">
        <p class="text-danger">目前暂时没有进行中的借款</p>
    </td>
</tr>
</#if>
