import {PeopleItem} from "./people/peopleItem";
import {Card, CardBody, CardHeader, CardTitle, Container} from "react-bootstrap";


// нужен запрос
const peopleData = [
    {name: 'Иванов Юрий Петрович'},
    {name: 'Порпоппо ОПП ЛПНПнп'},
    {name: 'ПРПР рпПп РПНПНРПРО'},
    {name: 'врврвр вроврврв воврвр вовр'}
];

export const PeopleList = () => {
    return (
        <Container className='mt-5'>
            <Card>
                <CardHeader>
                    <CardTitle>Пользователи</CardTitle>
                </CardHeader>
                <CardBody>
                    {peopleData.map((Data, index) => (
                        <PeopleItem
                            key={index}
                            name={Data.name}
                        />
                    ))}
                </CardBody>
            </Card>

        </Container>
    )
}