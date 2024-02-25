import {Col, ListGroup, Row} from "react-bootstrap";
import {useMutation, useQueryClient} from "react-query";
import UserService from "../../../services/UserService";
import {ButtonStudent} from "./people/studentRoleButton";
import {ButtonTeacher} from "./people/teacherRoleButton";

export const PeopleAccordionItem = ({user}) => {
    const {clientId, roles} = user
    const queryClient = useQueryClient()
    const mutation = useMutation((params) => UserService.addRole(params.clientId, params.clientRole), {
        onSuccess() {
            queryClient.invalidateQueries(['users'])
        }
    })

    const handleAddRole = (clientRole) => {
        mutation.mutate({clientId, clientRole})
    }

    if (roles.includes('UNSPECIFIED')) {
        return (
            <ListGroup.Item key={user.clientId}
                            className={'border-0 border-bottom mt-2'}>
                <Row className={'w-100 p-0 m-0'}>
                    <Col sm={12} md={4} className={'p-0'}>
                        <div className={'text-center text-md-start'}>
                            <span className={'fw-bold d-md-none'}>ФИО:</span>
                            {user.name}
                        </div>
                    </Col>
                    <Col sm={12} md={4} className={'p-0'}>
                        <div className={'text-center text-md-start mt-2 mb-2 m-md-0'}>
                            <span className={'fw-bold d-md-none'}>Email:</span>
                            {user.email}
                        </div>

                    </Col>
                    <Col sm={12} md={4} className={'p-0'}>
                        <div className={'d-flex gap-2 justify-content-center justify-content-md-end'}>
                            <ButtonStudent callback={() => handleAddRole(['STUDENT'])}/>
                            <ButtonTeacher callback={() => handleAddRole(['TEACHER'])}/>
                        </div>
                    </Col>
                </Row>


            </ListGroup.Item>
        )
    }

    return (
        <ListGroup.Item key={user.clientId}
                        className={'border-0 border-bottom mt-2 d-flex justify-content-between'}>
            <span>{user.name}</span>
            <span>{user.email}</span>
        </ListGroup.Item>
    )
}