import {useQuery} from "react-query";
import BuildingService from "../../../services/BuildingService";
import {Loading} from "../../snippets/Loading";
import {Accordion, Card, Container, ListGroup} from "react-bootstrap";

export const BuildingPage = () => {

    const {data, isLoading, error} = useQuery(['buildings'], () => BuildingService.getAccomodations(), {
        refetchOnWindowFocus: false,
        keepPreviousData: false,
        select: ({data}) => {
            return data.reduce((acc, curr) => {
                const {room, build} = curr

                if (!acc[build]) {
                    acc[build] = []
                }
                acc[build].push(room)
                return acc
            }, {})
        }
    })


    if (isLoading) {
        return (
            <Loading/>
        )
    }

    console.log(data)

    return (
        <Container className={'mt-5'}>
            <Accordion>
                {Object.keys(data).map((build) => {
                    return (
                        <Accordion.Item  eventKey={build}>
                                <Accordion.Header>Корпус {build}</Accordion.Header>
                            <Accordion.Body>
                                <ListGroup>
                                    {data[build].map((room) => {
                                        return(
                                            <ListGroup.Item className={'border-0 border-bottom mt-2'}>Аудитория {room}</ListGroup.Item>
                                        )
                                    })}
                                </ListGroup>
                            </Accordion.Body>
                        </Accordion.Item>
                    )
                })}
            </Accordion>

        </Container>
    )
}