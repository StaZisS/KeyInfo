import {PeopleItem} from "./people/peopleItem";
import {Accordion, Card, CardBody, CardHeader, CardTitle, Collapse, Container, ListGroup} from "react-bootstrap";
import {useQuery} from "react-query";
import UserService from "../../../services/UserService";
import {Loading} from "../../snippets/Loading";
import {useState} from "react";
import {PeopleAccordionItem} from "./PeopleAccordionItem";
import {ListHeader} from "./ListHeader";


export const PeopleList = () => {

    const [unspOpen, setUnspOpen] = useState(false)
    const [tchOpen, setTchOpen] = useState(false)
    const [stdsOpen, setStdOpen] = useState(false)

    const {data, isLoading, error} = useQuery('users', UserService.getUsers, {
        select: ({data}) => ({
            UNSPECIFIED: data.filter(user => user.roles.includes('UNSPECIFIED')),
            TEACHERS: data.filter(user => user.roles.includes('TEACHER')),
            STUDENTS: data.filter(user => user.roles.includes('STUDENT')),
        }),
        refetchOnWindowFocus: false,
        keepPreviousData: true
    },)


    if (isLoading) {
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
                        <ListHeader/>
                        <ListGroup>
                            {data.UNSPECIFIED.map((user) => {
                                return (
                                    <PeopleAccordionItem user={user}/>
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
                                    <PeopleAccordionItem user={user}/>
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
                                    <PeopleAccordionItem user={user}/>
                                )
                            })}
                        </ListGroup>
                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>
        </Container>

    )
}