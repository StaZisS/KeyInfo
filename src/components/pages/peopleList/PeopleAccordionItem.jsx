import {ListGroup} from "react-bootstrap";

export const PeopleAccordionItem = ({user}) => {
    return (
        <ListGroup.Item key={user.clientId}
                        className={'border-0 border-bottom mt-2 d-flex justify-content-between'}>
            <span>{user.name}</span>
            <span>{user.email}</span>
        </ListGroup.Item>
    )
}