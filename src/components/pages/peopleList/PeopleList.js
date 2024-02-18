import {PeopleItem} from "./people/peopleItem";
import {Card, CardBody, CardHeader, CardTitle, Collapse, Container} from "react-bootstrap";
import {useQuery} from "react-query";
import UserService from "../../../services/UserService";
import {Loading} from "../../snippets/Loading";
import {useState} from "react";


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

    if (error) {
        return (
            <>Ошибка фетча пользователей</>
        )
    }


    return (
        <Container className='mt-5'>
            <Card className={'rounded-0'}>
                <CardHeader onClick={() => setUnspOpen(!unspOpen)}>
                    <CardTitle className='user-select-none'>Не верифицированные пользователи ({data.UNSPECIFIED.length})</CardTitle>
                </CardHeader>
                <Collapse in={unspOpen}>
                    <div>
                        <CardBody>
                            {data.UNSPECIFIED.map((user) => (
                                <PeopleItem
                                    key={user.clientId}
                                    name={user.name}
                                    email={user.email}
                                    userId={user.clientId}
                                    roles={'UNSPECIFIED'}
                                />
                            ))}
                        </CardBody>
                    </div>
                </Collapse>
            </Card>

            <Card className={'rounded-0 mt-3'}>
                <CardHeader onClick={() => setTchOpen(!tchOpen)}>
                    <CardTitle className='user-select-none'>Преподаватели ({data.TEACHERS.length})</CardTitle>
                </CardHeader>
                <Collapse in={tchOpen}>
                    <div>
                        <CardBody>
                            {data.TEACHERS.map((user) => (
                                <PeopleItem
                                    key={user.clientId}
                                    name={user.name}
                                    email={user.email}
                                    userId={user.clientId}
                                    roles={'TEACHER'}
                                />
                            ))}
                        </CardBody>
                    </div>
                </Collapse>
            </Card>

            <Card className={'rounded-0 mt-3'}>
                <CardHeader onClick={() => setStdOpen(!stdsOpen)}>
                    <CardTitle className='user-select-none'>Студенты ({data.STUDENTS.length})</CardTitle>
                </CardHeader>
                <Collapse in={stdsOpen}>
                    <div>
                        <CardBody>
                            {data.STUDENTS.map((user) => (
                                <PeopleItem
                                    key={user.clientId}
                                    name={user.name}
                                    email={user.email}
                                    userId={user.clientId}
                                    roles={'STUDENT'}
                                />
                            ))}
                        </CardBody>
                    </div>
                </Collapse>

            </Card>

        </Container>
    )
}