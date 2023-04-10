const listNode = document.getElementById('list')

function buildListItemNode(item) {
    itemNode = document.createElement('div')
    itemNode.classList.add('list-item');

    textNode = document.createElement('span')
    textNode.innerText = item.message

    editButton = document.createElement('button')
    editButton.innerText = 'Edit'
    editButton.classList.add('btn-primary')

    deleteButton = document.createElement('button')
    deleteButton.innerText = 'Delete'
    deleteButton.classList.add('btn-delete', 'btn-primary')

    itemNode.appendChild(textNode)
    itemNode.appendChild(editButton)
    itemNode.appendChild(deleteButton)

    return itemNode
}

function loadList(list) {
    list.forEach(item => {
        listNode.appendChild(buildListItemNode(item))
    })
}
