// 简单的 JSON 高亮
function syntaxHighlight(json) {
    if (typeof json !== 'string') {
        json = JSON.stringify(json, undefined, 2);
    } else {
        try {
            // 如果是 JSON 字符串，同样保证缩进为 2
            var jsonObj = JSON.parse(json);
            json = JSON.stringify(jsonObj, undefined, 2);
        } catch (e) {
            console.error("提供的字符串不是有效的JSON格式:", e);
        }
    }

    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');

    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        let cls = 'json-value';

        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'json-key';
            } else {
                cls = 'json-string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'json-boolean';
        } else if (/null/.test(match)) {
            cls = 'json-null';
        }

        return '<span class="' + cls + '">' + match + '</span>';
    });
}

async function fetchWithSyntaxHighlight(url, options) {
    let result = '';
    try {
        const response = await fetch(url, options);
        // 检查响应状态
        if (!response.ok) {
            // 如果携带错误信息，展示错误信息
            const errResponse = await response.text()
            if (errResponse) {
                throw new Error(errResponse);
            }
            // 否则展示错误码
            throw new Error(`HTTP Error: ${response.status}`);
        }
        result = await response.json();
    } catch (error) {
        result = error.message;
    }
    // 高亮展示
    return syntaxHighlight(result);
}

// 发送 GET 请求
async function get(url) {
    return fetchWithSyntaxHighlight(url);
}

// 发送 GET 请求
async function post(url, data) {
    const options = {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(data)
    };
    return fetchWithSyntaxHighlight(url, options);
}