import {PeopleItem} from "./people/peopleItem";
import {Accordion, Card, CardBody, CardHeader, CardTitle, Collapse, Container, ListGroup} from "react-bootstrap";
import {useQuery} from "react-query";
import UserService from "../../../services/UserService";
import {Loading} from "../../snippets/Loading";
import {useState} from "react";
import {PeopleAccordionItem} from "./PeopleAccordionItem";
import {ListHeader} from "./ListHeader";
import AuthService from "../../../services/AuthService";


export const PeopleList = () => {

    const {data, isLoading, error} = useQuery('users', UserService.getUsers, {
        select: ({data}) => ({
            UNSPECIFIED: data.filter(user => user.roles.includes('UNSPECIFIED')),
            TEACHERS: data.filter(user => user.roles.includes('TEACHER')),
            STUDENTS: data.filter(user => user.roles.includes('STUDENT')),
            DEANERIES: data.filter(user => user.roles.includes('DEANERY')),
        }),
        refetchOnWindowFocus: false,
        keepPreviousData: true
    },)

    const {data : role, isLoading : isLoadingProfile, error : errorProfile} = useQuery('profile', AuthService.getProfile, {
        select({data}){
            return data.roles
        },
        refetchOnWindowFocus: false,
        keepPreviousData: true
    },)


    if (isLoading || isLoadingProfile) {
        return (
            <Loading/>
        )
    }

    return (
        <Container className='mt-5'>
            <Accordion>
                <Accordion.Item eventKey={'UNSPECIFIED'}>
                    <Accordion.Header>Неверифицированные пользователи ({data.UNSPECIFIED.length})</Accordion.Header>
                    <Accordion.Body>
                        <div className={'d-flex justify-content-between border-bottom'}>
                            <span className={'ms-3 fw-bold d-none d-md-block flex-grow-1'}>ФИО</span>
                            <span className={'fw-bold text-start d-none d-md-block flex-grow-1'}>Email</span>
                            <span className={'me-3 fw-bold text-end d-none d-md-block flex-grow-1 '}>Назначить</span>
                        </div>
                        <ListGroup>
                            {data.UNSPECIFIED.map((user) => {
                                return (
                                    <PeopleAccordionItem role={role} user={user} key={user.clientId}/>
                                )
                            })}
                        </ListGroup>
                    </Accordion.Body>
                </Accordion.Item>
                <Accordion.Item eventKey={'TEACHER'}>
                    <Accordion.Header>Преподаватели ({data.TEACHERS.length})</Accordion.Header>
                    <Accordion.Body>
                        <ListHeader/>
                        <ListGroup>
                            {data.TEACHERS.map((user) => {
                                return (
                                    <PeopleAccordionItem user={user} key={user.clientId}/>
                                )
                            })}
                        </ListGroup>
                    </Accordion.Body>
                </Accordion.Item>
                <Accordion.Item eventKey={'STUDENT'}>
                    <Accordion.Header>Студенты ({data.STUDENTS.length})</Accordion.Header>
                    <Accordion.Body>
                        <ListHeader/>
                        <ListGroup>
                            {data.STUDENTS.map((user) => {
                                return (
                                    <PeopleAccordionItem user={user} key={user.clientId}/>
                                )
                            })}
                        </ListGroup>
                    </Accordion.Body>
                </Accordion.Item>
                <Accordion.Item eventKey={'DEANERY'}>
                    <Accordion.Header>Деканат ({data.DEANERIES.length})</Accordion.Header>
                    <Accordion.Body>
                        <ListHeader/>
                        <ListGroup>
                            {data.DEANERIES.map((user) => {
                                return (
                                    <PeopleAccordionItem user={user} key={user.clientId}/>
                                )
                            })}
                        </ListGroup>
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        </Container>

    )
}