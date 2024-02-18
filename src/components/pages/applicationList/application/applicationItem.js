import {Button, Card, CardBody, CardHeader, CardText, CardTitle, Col, Container, Row} from "react-bootstrap";
import {RxReload} from "react-icons/rx";

import('../../../../styles/applicationItem.css')

export const ApplicationItem = ({build, room, name, email, start, end, dublicate}) => {
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
                                {dublicate && <RxReload/>}


                            </Col>
                            <Col lg={4} md={6} sm={12} className={'d-flex gap-3 justify-content-md-end'}>
                                <CardTitle> Здание <span className='fw-bold'>{build}</span></CardTitle>
                                <CardTitle>Аудитория <span className='fw-bold'>{room}</span></CardTitle>
                            </Col>
                        </Row>
                    </CardHeader>
                    <CardBody className={'d-flex align-items-center'}>
                        <Row className='w-100'>

                            <Col xs={12} md={8} lg={8} className={'d-flex card-text gap-3 align-items-end'}>
                                <span className='text-muted'>Фио: <span
                                    className={'fs-5 text-black'}>{name}</span> </span>
                                <span className='text-muted'>Email: <span
                                    className={'fs-5 text-black'}>{email}</span> </span>
                            </Col>

                            <Col xs={12} md={1} lg={4}
                                 className={'ms-auto d-flex gap-3 mt-sm-3 justify-content-sm-end'}>
                                <Button className='btn-success'>Одобрить</Button>
                                <Button className='btn-danger'>Отклонить</Button>
                            </Col>
                        </Row>

                    </CardBody>
                </Card>
            </Container>

        </>
    )
}