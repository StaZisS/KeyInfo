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


    return (
        <Container className={'mt-5'}>
            <Accordion>
                {Object.keys(data).map((build) => {
                    return (
                        <Accordion.Item  eventKey={build} key={build}>
                                <Accordion.Header>Корпус {build}</Accordion.Header>
                            <Accordion.Body>
                                <ListGroup>
                                    <div className={'border-bottom'}>
                                        <span className={'ms-3 fw-bold'}>Аудитория</span>
                                    </div>
                                    {data[build].map((room) => {
                                        return(
                                            <ListGroup.Item key={`${build}_${room}`} className={'border-0 border-bottom mt-2'}>{room}</ListGroup.Item>
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