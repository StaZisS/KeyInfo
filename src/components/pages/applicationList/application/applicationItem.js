import {Button, Card, CardBody, CardHeader, CardText, CardTitle, Col, Container, Row} from "react-bootstrap";
import {RxReload} from "react-icons/rx";
import {useMutation, useQueryClient} from "react-query";
import ApplicationService from "../../../../services/ApplicationService";

import('../../../../styles/applicationItem.css')

export const ApplicationItem = ({id,build, room, name, email, start, end, dublicate}) => {

    const queryClient = useQueryClient()
    const acceptMutation = useMutation((id) => ApplicationService.acceptApplication(id), {
        onSuccess() {
            queryClient.invalidateQueries(['applications'])
        }
    })

    const declineMutation = useMutation((id) => ApplicationService.declineApplication(id), {
        onSuccess() {
            queryClient.invalidateQueries(['applications'])
        }
    })

    return (
        <>
            <Container className='mt-3'>
                <Card>
                    <CardHeader className='container-fluid '>
                        <Row className='w-100'>
                            <Col lg={8} md={6} className={'d-flex gap-3'}>
                                <CardTitle> {start.day}.{start.month}.{start.year}</CardTitle>
                                <CardTitle
                                    className='fw-bold'> {start.hours}:{start.minutes} - {end.hours}:{end.minutes}
                                </CardTitle>
                                {dublicate && <RxReload title={'Повторяющаяся заявка'}/>}


                            </Col>
                            <Col lg={4} md={6} sm={12} className={'d-flex gap-3 justify-content-md-end'}>
                                <CardTitle> Здание <span className='fw-bold'>{build}</span></CardTitle>
                                <CardTitle>Аудитория <span className='fw-bold'>{room}</span></CardTitle>
                            </Col>
                        </Row>
                    </CardHeader>
                    <CardBody>
                        <Row className='w-100 d-flex align-items-center'>

                            <Col xs={12} md={8} lg={8} className={'d-flex card-text gap-3 align-items-end'}>
                                <span className='text-muted'>Фио: <span
                                    className={'fs-5 text-black'}>{name}</span> </span>
                                <span className='text-muted'>Email: <span
                                    className={'fs-5 text-black'}>{email}</span> </span>
                            </Col>

                            <Col xs={12} md={1} lg={4}
                                 className={'ms-auto d-flex gap-3 mt-3 mt-sm-0 justify-content-end'}>
                                <Button onClick={() => acceptMutation.mutate(id)} className='btn-success'>Одобрить</Button>
                                <Button onClick={() => declineMutation.mutate(id)} className='btn-danger'>Отклонить</Button>
                            </Col>
                        </Row>

                    </CardBody>
                </Card>
            </Container>

        </>
    )
}